#!/usr/bin/python

import socket
import sys
import time
import struct
from clueless import *

class BadSocketException(Exception):
    pass

def send_u8(s, byte):
    data = struct.pack('B', byte)
    s.sendall(data)

def send_s8(s, byte):
    data = struct.pack('b', byte)
    s.sendall(data)

def recv_u8(s):
    byte = s.recv(1)
    if len(byte) == 0:
        raise BadSocketException("Bad Socket")
    return struct.unpack('B', byte)[0]

def parse_state(client):
    mode = recv_u8(client)
    for i in range(0, 6):
        id = recv_u8(client)
        used = recv_u8(client)
        rtype = recv_u8(client)
        north = recv_u8(client)
        south = recv_u8(client)
        east = recv_u8(client)
        west = recv_u8(client)
        other = recv_u8(client)
        
        if not used:
            print "%s is not active in room %s" % \
                (suspect_strings[id], room_strings[rtype])
        else:
            print "%s is in %s (N %s S %s E %s W %s O %s)" % \
                (suspect_strings[id], room_strings[rtype],
                 room_strings[north], room_strings[south],
                 room_strings[east], room_strings[west],
                 room_strings[other])
    if mode != MODE_CHARSEL:
        target = recv_u8(client)
        accused_id = recv_u8(client)
        weapon_id = recv_u8(client)
        location_id = recv_u8(client)
        
        print "mode %s target %d A %d W %d L %d" % (hex(mode), target, accused_id, weapon_id, location_id)
        
        
        #print("id %s used %s pos %s" % (suspect_strings[id], used, pos))

def parse_charsel(client):
    claimed = recv_u8(client)
    if claimed:
        print "Character secessfully claimed."
    else:
        print "Failed to claim character"

def parse_start(client):
    started = recv_u8(client)
    if started:
        print "Game started."
    else:
        print "Must choose character to start game."
 
def parse_move(client):
    ack = recv_u8(client)
    if ack == 0:
        print "Move invalid."
    elif ack == 1:
        print "Move complete."
    else:
        accused_result = recv_u8(client)
        weapon_result = recv_u8(client)
        print "Suggestion made."
 
def state_request():
    return struct.pack('B', ACTION_STATE_REQ)

def start_request():
    return struct.pack('B', ACTION_START_REQ)

def move_request(direction, accused, weapon):
    state = [ACTION_MOVE_REQ]
    state.append(direction)
    state.append(accused)
    state.append(weapon)
    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)
        
def charsel_request(suspect_id):
    state = [ACTION_CHARSEL_REQ]
    state.append(suspect_id)
    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)

# TODO: Need a way to assist the client here to not enable lying
# For now, the field with the non-zero id is the valid card
def disprove_request(accused_id, weapon_id, location_id):
    state = [ACTION_DISPROVE_REQ]
    state.append(accused_id)
    state.append(weapon_id)
    state.append(location_id)
    fmt = '%d' % len(state) + 'B'
    return struct.pack(fmt, *state)

server_address = ('localhost', 8000)

# Create a TCP/IP socket
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect the socket to the port where the server is listening
print('connecting to %s port %s' % server_address)
client.connect(server_address)

# Get state command
client.sendall(state_request())
# State response
if recv_u8(client) == ACTION_STATE_RES:
    parse_state(client)

# Select character command
client.sendall(charsel_request(SUSPECT_GREEN))
# Select character response
if recv_u8(client) == ACTION_CHARSEL_RES:
    parse_charsel(client)

# Select 2nd character command (should fail)
client.sendall(charsel_request(SUSPECT_PLUM))
# Select character response
if recv_u8(client) == ACTION_CHARSEL_RES:
    parse_charsel(client)
    
# Start the game
client.sendall(start_request())
# Select character response
if recv_u8(client) == ACTION_START_RES:
    parse_start(client)
    
# Move GREEN north (with noop accused and noop weapon)
client.sendall(move_request(DIR_NORTH, SUSPECT_PLUM, WEAPON_PIPE))
if recv_u8(client) == ACTION_MOVE_RES:
    parse_move(client)

# Get state after move
client.sendall(state_request())
# State response
if recv_u8(client) == ACTION_STATE_RES:
    parse_state(client)

# Move GREEN north (should fail)
client.sendall(move_request(DIR_NORTH, SUSPECT_PLUM, WEAPON_PIPE))
if recv_u8(client) == ACTION_MOVE_RES:
    parse_move(client)

# Move GREEN west (with noop accused and noop weapon)
client.sendall(move_request(DIR_WEST, SUSPECT_PLUM, WEAPON_PIPE))
if recv_u8(client) == ACTION_MOVE_RES:
    parse_move(client)

time.sleep(3)













