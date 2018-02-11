#!/usr/bin/python

import sys
import select
import socket
import struct
from clueless import *

NO_CLIENT = 0xFF
clients = {}
outputs = []

game_state = GameState()
    
class SvcClient(object):
    def __init__(self, socket = None, address = None):
        self.socket = socket
        self.address = address
        self.outgoing = ""
        self.suspect_id = None
    def sendall(self):
        self.socket.sendall(self.outgoing)
        self.outgoing = ""

class BadSocketException(Exception):
    pass

def send_u8(s, byte):
    data = struct.pack('B', byte)
    s.sendall(data)

def recv_u8(s):
    byte = s.recv(1)
    if len(byte) == 0:
        raise BadSocketException("Bad Socket")
    return struct.unpack('B', byte)[0]

def send_s8(s, byte):
    data = struct.pack('b', byte)
    s.sendall(data)

def serialize_state(client):
    state = [ACTION_STATE_RES, game_state.mode]
    for suspect in game_state.suspects.values():
        state.append(suspect.id)
        if suspect.claimed:
            state.append(0x1)
        else:
            state.append(0x0)
        
        # room type
        if suspect.location is ROOM_INVALID:
            state.append(ROOM_INVALID)
        else:
            state.append(suspect.location.id)
        
        for dir in [DIR_NORTH, DIR_SOUTH, DIR_EAST, DIR_WEST, DIR_OTHER]:
            if not suspect.location or \
               not suspect.location.directions.has_key(dir) or \
               not suspect.location.directions[dir]:
                state.append(ROOM_INVALID)
            else:
                state.append(suspect.location.directions[dir].id)
    
    # Only do the disprove announcements after character selection
    if game_state.mode != MODE_CHARSEL:
        if game_state.inquisition_cursor:
            state.append(game_state.inquisition_cursor.id)
        else:
            state.append(0)
        state.append(game_state.accused_id)
        state.append(game_state.weapon_id)
        state.append(game_state.location_id)
    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)

def serialize_charsel_response(client):
    suspect_id = recv_u8(client.socket)
    # TODO: Validate suspect_id
    suspect = game_state.suspects[suspect_id]
    state = [ACTION_CHARSEL_RES]
    if client.suspect_id is None and not suspect.claimed:  
        suspect.claimed = True
        suspect.client = client
        client.suspect_id = suspect_id
        if game_state.add_active_suspect(suspect):
            state.append(0x01)
        else:
            state.append(0x00)
    else:
        state.append(0x00)
    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)
  
def serialize_bytes(byte_list):
    fmt = '%d' % len(byte_list) + 'B'
    return struct.pack(fmt, *byte_list)

def end_turn(byte_list):
    # Set next persons turn
    game_state.next_turn()
    return serialize_bytes(byte_list)
    
def handle_move_response(client):
    direction = recv_u8(client.socket)
    accused_id = recv_u8(client.socket)
    weapon_id = recv_u8(client.socket)
    # TODO: Validate input values
    suspect = game_state.suspects[client.suspect_id]
    state = [ACTION_MOVE_RES]

    # If suspect is in an invalid room, it must be their first turn
    if suspect.location is ROOM_INVALID:    
        suspect.location = suspect.start_location
        suspect.location.suspects.append(suspect)
        # Return move success
        state.append(0x01)
        return end_turn(state)
    
    # If suspect doesn't match game_state cursor, its not their turn
    if not game_state.is_turn(suspect):
        # not client's turn
        state.append(0x00) # return move failed
        return serialize_bytes(state)

    # If suspect doesn't have a valid direction or the direction
    # goes to an invalid room, the move can not take place.
    if not suspect.location.directions.has_key(direction) or \
       not suspect.location.directions[direction]:
        # invalid direction
        print "invalid direction"
        state.append(0x00)
        return serialize_bytes(state)
    
    # Can not move because the space is occupied
    # TODO ??? How many can fit in a room?
    if suspect.location.id == ROOM_HALLWAY and \
       len(suspect.location.directions[direction].suspects) > 0:
        # occupied
        state.append(0x00)
        return serialize_bytes(state)

    suspect.move_to(suspect.location.directions[direction])
    
    # If we're in a room we must suggest
    if game_state.rooms.has_key(suspect.location.id):
        handle_suggestion(suspect, accused_id, weapon_id)
        # Unless we just won, we're likely waiting.
        state.append(0x02)
    else:
        # Probably a hallway, move along
        state.append(0x01)
    return end_turn(state)

def handle_suggestion(suspect, accused_id, weapon_id):
    # Put the accused in the current location (if not there)
    accused = game_state.suspects[accused_id]
    if accused.location is not suspect.location:
        accused.move_to(suspect.location)
                
    # 1. Find next player that may disprove
    player_inquisition = game_state.player_cursor.next
    found = False
    while player_inquisition != game_state.player_cursor:
        for card in player_inquisition.cards:
            if (card == accused_id or card == weapon_id or card == suspect.location.id):
                found = True
                break
        # iterate
        if found:
            break
        player_inquisition = player_inquisition.next

    if not found:
        # 2. Did we have the cards?
        for card in suspect.cards:
            if card == accused_id or card == weapon_id or card == suspect.location.id:
                return
        # TODO: Handle this better.
        print "YOU WIN"
        sys.exit(0)
            
    # 3. Ask player to choose a card to disprove
    game_state.mode = MODE_DISPROVE;
    return

# TODO: handle_disprove
# Note the disprove in the notebook
    
def serialize_start_response(client):
    # Do we have at least 1 player?
    state = [ACTION_START_RES]
    for suspect in game_state.suspects.values():
        if suspect.claimed:
            game_state.start()
                
    if game_state.started:
        state.append(1)
    else:
        state.append(0)

    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)
    
def process_client_request(client):
    s = client.socket
    msg_id = recv_u8(s)
    if msg_id == ACTION_STATE_REQ:
        client.outgoing = serialize_state(client)
        if s not in outputs:
            outputs.append(s)
    elif msg_id == ACTION_CHARSEL_REQ:
        # TODO: Does character exist?
        # Is character claimed?
        client.outgoing = serialize_charsel_response(client)
        if s not in outputs:
            outputs.append(s)
    elif msg_id == ACTION_START_REQ:
        client.outgoing = serialize_start_response(s)
        if s not in outputs:
            outputs.append(s)
    elif msg_id == ACTION_MOVE_REQ:
        client.outgoing = handle_move_response(client)
        if s not in outputs:
            outputs.append(s)    
            
        
def main():

    svc_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    svc_socket.setblocking(0)

    #host = socket.gethostname()
    host = "127.0.0.1"
    port = 8000
    svc_socket.bind((host, port))
    svc_socket.listen(5)
    print('listening from %s' % host)

    while True:
        inputs = [ svc_socket ] + clients.keys()
        timeout = 10
        # Wait for at least one of the sockets to be ready for processing
        rfds, wfds, efds = select.select(inputs, outputs, inputs, timeout)
        for s in rfds:
            if s is svc_socket:
                print('Accepting new connection')
                (c, addr) = svc_socket.accept()
                c.setblocking(0)
                clients[c] = SvcClient(socket = c, address = addr)
                continue
            # If we're not svc_socket, we're a client.
            try:
                process_client_request(clients[s])
            except BadSocketException:
                clients.pop(s, None)
                s.close()
                pass
        for s in wfds:
            clients[s].sendall()
            outputs.remove(s)


main()