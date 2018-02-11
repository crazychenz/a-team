#!/usr/bin/python

import random

# Set room_id constants
ROOM_INVALID = 0x00
ROOM_HALLWAY = 0x01
ROOM_STUDY = 0x10
ROOM_HALL = 0x11
ROOM_LOUNGE = 0x12
ROOM_LIBRARY = 0x13
ROOM_BILLIARDS = 0x14
ROOM_DINNING = 0x15
ROOM_CONSERVATORY = 0x16
ROOM_BALLROOM = 0x17
ROOM_KITCHEN = 0x18

room_strings = {
    ROOM_INVALID: "Invalid",
    ROOM_HALLWAY: "Hallway",
    ROOM_STUDY: "Study",
    ROOM_HALL: "Hall",
    ROOM_LOUNGE: "Lounge",
    ROOM_LIBRARY: "Library",
    ROOM_BILLIARDS: "Billiards",
    ROOM_DINNING: "Dinning",
    ROOM_CONSERVATORY: "Conservatory",
    ROOM_BALLROOM: "Ballroom",
    ROOM_KITCHEN: "Kitchen",
}

# Set suspect_id constants
# Note: These are in order starting with
#       Scarlet and moving to the player's
#       left
SUSPECT_SCARLET = 0x20;
SUSPECT_MUSTARD = 0x21;
SUSPECT_WHITE = 0x22;
SUSPECT_GREEN = 0x23;
SUSPECT_PEACOCK = 0x24;
SUSPECT_PLUM = 0x25;

suspect_strings = {
    SUSPECT_PLUM: "Plum",
    SUSPECT_SCARLET: "Scarlet",
    SUSPECT_GREEN: "Green",
    SUSPECT_MUSTARD: "Mustard",
    SUSPECT_WHITE: "White",
    SUSPECT_PEACOCK: "Peacock",
}

ACTION_DISCONNECT = 0x30; # disconnect message
ACTION_CONNECT = 0x31;    # connection message
ACTION_CHARSEL_REQ = 0x32; # request character
ACTION_CHARSEL_RES = 0x33; # character selection response
ACTION_STATE_REQ = 0x35; # request/respond with character selection state
ACTION_STATE_RES = 0x36; # request/respond with character selection state
ACTION_START_REQ = 0x37; # start game
ACTION_START_RES = 0x38;
ACTION_MOVE_REQ = 0x39;
ACTION_MOVE_RES = 0x3A;
ACTION_DISPROVE_REQ = 0x3B
ACTION_DISPROVE_RES = 0x3C
ACTION_ACCUSE_REQ = 0x3D
ACTION_ACCUSE_RES = 0x3E

WEAPON_PIPE = 0x50
WEAPON_REVOLVER = 0x51
WEAPON_ROPE = 0x52
WEAPON_CANDLE = 0x53
WEAPON_KNIFE = 0x54
WEAPON_WRENCH = 0x55

DIR_NORTH = 0x40
DIR_SOUTH = 0x41
DIR_EAST = 0x42
DIR_WEST = 0x43
DIR_OTHER = 0x44

MODE_NORMAL = 0x60
MODE_DISPROVE = 0x61
MODE_CHARSEL = 0x62

class Notebook(object):
    def __init__(self):
        self.cards = {
            ROOM_STUDY: None,
            ROOM_HALL: None,
            ROOM_LOUNGE: None,
            ROOM_LIBRARY: None,
            ROOM_BILLIARDS: None,
            ROOM_DINNING: None,
            ROOM_CONSERVATORY: None,
            ROOM_BALLROOM: None,
            ROOM_KITCHEN: None,
            SUSPECT_PLUM: None,
            SUSPECT_SCARLET: None,
            SUSPECT_GREEN: None,
            SUSPECT_MUSTARD: None,
            SUSPECT_WHITE: None,
            SUSPECT_PEACOCK: None,
            WEAPON_PIPE: None,
            WEAPON_REVOLVER: None,
            WEAPON_ROPE: None,
            WEAPON_CANDLE: None,
            WEAPON_KNIFE: None,
            WEAPON_WRENCH: None,
        }

class Suspect(object):
    def __init__(self, id, start_location):
        self.id = id
        self.start_location = start_location
        self.location = ROOM_INVALID
        self.claimed = False
        self.next = None
        self.cards = []
        self.notebook = Notebook()

    def move_to(self, destination):
        # Note: Assumed caller checked validity of move first
        
        # only leave old location if its not first turn
        if self.location is not ROOM_INVALID:
            # leave old location
            self.location.suspects.remove(self)
        # enter new location
        destination.suspects.append(self)
        # record where we went
        self.location = destination

class CardDeck(object):
    def __init__(self):
        self.room_cards = {
            ROOM_STUDY: None,
            ROOM_HALL: None,
            ROOM_LOUNGE: None,
            ROOM_LIBRARY: None,
            ROOM_BILLIARDS: None,
            ROOM_DINNING: None,
            ROOM_CONSERVATORY: None,
            ROOM_BALLROOM: None,
            ROOM_KITCHEN: None,
        }
        
        self.suspect_cards = {
            SUSPECT_PLUM: None,
            SUSPECT_SCARLET: None,
            SUSPECT_GREEN: None,
            SUSPECT_MUSTARD: None,
            SUSPECT_WHITE: None,
            SUSPECT_PEACOCK: None,
        }
        
        self.weapon_cards = {
            WEAPON_PIPE: None,
            WEAPON_REVOLVER: None,
            WEAPON_ROPE: None,
            WEAPON_CANDLE: None,
            WEAPON_KNIFE: None,
            WEAPON_WRENCH: None,
        }

        # TODO: Set seed.
        
        # Shuffle weapon cards
        weapon_deck = []
        self.shuffle_in(weapon_deck, self.weapon_cards.keys())
        
        # Shuffle suspect cards
        suspect_deck = []
        self.shuffle_in(suspect_deck, self.suspect_cards.keys())
        
        # Shuffle room cards
        room_deck = []
        self.shuffle_in(room_deck, self.room_cards.keys())
        
        # Deal cards to envelope
        self.envelope = {}
        self.envelope["who"] = suspect_deck.pop()
        self.envelope["what"] = weapon_deck.pop()
        self.envelope["where"] = room_deck.pop()
        
        # Shuffle remaining cards for player deal
        self.player_deck = []
        player_cards = weapon_deck + suspect_deck + room_deck
        self.shuffle_in(self.player_deck, player_cards)

    def deal(self, players):
        while len(self.player_deck) > 0:
            for player in players:
                if len(self.player_deck) == 0:
                    return
                idx = random.randint(0, len(self.player_deck)-1)
                card = self.player_deck.pop(idx)
                player.cards.append(card)

                # Mark in our notebook the cards we've seen
                player.notebook.cards[card] = True
        
        
    def shuffle_in(self, dst_deck, src_deck):
        cpy_deck = src_deck[:]
        while len(cpy_deck):
            idx = random.randint(0, len(cpy_deck)-1)
            dst_deck.append(cpy_deck.pop(idx))
      
class Location(object):
    def __init__(self, id, north = ROOM_INVALID, south = ROOM_INVALID, 
                 east = ROOM_INVALID, west = ROOM_INVALID, other = ROOM_INVALID):
        self.id = id
        
        self.suspects = []
        
        self.directions = {}
        
        self.connect_north(north)
        self.connect_south(south)
        self.connect_east(east)
        self.connect_west(west)
        self.connect_other(other)

    def connect_north(self, north):
        self.directions[DIR_NORTH] = north
        if north:
            north.directions[DIR_SOUTH] = self

    def connect_south(self, south):
        self.directions[DIR_SOUTH] = south
        if south:
            south.directions[DIR_NORTH] = self

    def connect_east(self, east):
        self.directions[DIR_EAST] = east
        if east:
            east.directions[DIR_WEST] = self

    def connect_west(self, west):
        self.directions[DIR_WEST] = west
        if west:
            west.directions[DIR_EAST] = self

    def connect_other(self, other):
        self.directions[DIR_OTHER] = other
        if other:
            other.directions[DIR_OTHER] = self

class GameState(object):
    def __init__(self):
        self.started = False
        
        self.mode = MODE_CHARSEL
        
        # Normal Mode Iterables
        self.player_listhead = None
        self.player_cursor = None
        
        # Disprove Mode Iterables
        self.inquisition_cursor = None
        self.accused_id = 0
        self.weapon_id = 0
        self.location_id = 0
        
        self.active = [] # active suspects
        self.init_halls()
        self.init_rooms()
        self.init_suspects()

    def next_turn(self):
        self.player_cursor = self.player_cursor.next

    def is_turn(self, suspect):
        return self.player_cursor == suspect

    def add_active_suspect(self, suspect):
        if suspect in self.active:
            return False
        else:
            self.active.append(suspect)
            return True
        
    def start(self):
        self.mode = MODE_NORMAL
    
        # Shuffle and deal cards
        self.cards = CardDeck()
        self.cards.deal(self.active)
        
        # Sort player roster
        player_list = self.active[:]
        for suspect_id in [
            SUSPECT_SCARLET,
            SUSPECT_MUSTARD,
            SUSPECT_WHITE,
            SUSPECT_GREEN,
            SUSPECT_PEACOCK,
            SUSPECT_PLUM,
        ]:
            for player in self.active:
                # Clear linked list elem for next loop.
                player.next = None
                if player.id == suspect_id:
                    player_list.append(player)
        
        # Create ordered linked list of players
        self.player_listhead = player_list[0]
        self.player_cursor = self.player_listhead
        for player in player_list[1:]:
            self.player_cursor.next = player
            self.player_cursor = player
        self.player_cursor.next = self.player_listhead
        self.player_cursor = self.player_listhead
        
        # Start the game
        self.started = True
        
    def init_halls(self):
        # Generate the halls
        self.hall = []
        for i in range(0, 12):
            self.hall.append(Location(ROOM_HALLWAY))

    def init_rooms(self):
        self.rooms = {}
        rooms = self.rooms
        hall = self.hall

        # Connect rooms to halls
        rooms[ROOM_STUDY] = Location(ROOM_STUDY, east = hall[0], south = hall[2])
        rooms[ROOM_HALL] = Location(ROOM_HALL, east = hall[1], west = hall[0], south = hall[3])
        rooms[ROOM_LOUNGE] = Location(ROOM_LOUNGE, west = hall[1], south = hall[4])
        rooms[ROOM_LIBRARY] = Location(ROOM_LIBRARY, north = hall[2], east = hall[5], south = hall[7])
        rooms[ROOM_BILLIARDS] = Location(ROOM_BILLIARDS, north = hall[3], east = hall[6], south = hall[8], west = hall[5])
        rooms[ROOM_DINNING] = Location(ROOM_DINNING, north = hall[4], west = hall[6], south = hall[9])
        rooms[ROOM_CONSERVATORY] = Location(ROOM_CONSERVATORY, north = hall[7], east = hall[10])
        rooms[ROOM_BALLROOM] = Location(ROOM_BALLROOM, north = hall[8], east = hall[11], west = hall[10])
        rooms[ROOM_KITCHEN] = Location(ROOM_KITCHEN, north = hall[9], west = hall[11])

        # Connect secret passages
        rooms[ROOM_STUDY].connect_other(rooms[ROOM_KITCHEN])
        rooms[ROOM_LOUNGE].connect_other(rooms[ROOM_CONSERVATORY])

    def init_suspects(self):
        self.suspects = {}
        suspects = self.suspects
        hall = self.hall
        suspects[SUSPECT_PLUM] = Suspect(SUSPECT_PLUM, hall[2])
        suspects[SUSPECT_SCARLET] = Suspect(SUSPECT_SCARLET, hall[1])
        suspects[SUSPECT_GREEN] = Suspect(SUSPECT_GREEN, hall[10])
        suspects[SUSPECT_MUSTARD] = Suspect(SUSPECT_MUSTARD, hall[4])
        suspects[SUSPECT_WHITE] = Suspect(SUSPECT_WHITE, hall[11])
        suspects[SUSPECT_PEACOCK] = Suspect(SUSPECT_PEACOCK, hall[7])