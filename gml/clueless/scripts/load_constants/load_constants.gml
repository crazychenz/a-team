/// @description Insert description here
// You can write your code in this editor

// Some global configs
global.off_x = 64;
global.off_y = 64;
global.loc_width = 64;
global.loc_height = 64;

// Set room_id constants
global.ROOM_INVALID = 0x00
global.ROOM_VERT_HALLWAY = 0x01
global.ROOM_HORZ_HALLWAY = 0x02
global.ROOM_STUDY = 0x10
global.ROOM_HALL = 0x11
global.ROOM_LOUNGE = 0x12
global.ROOM_LIBRARY = 0x13
global.ROOM_BILLIARDS = 0x14
global.ROOM_DINNING = 0x15
global.ROOM_CONSERVATORY = 0x16
global.ROOM_BALLROOM = 0x17
global.ROOM_KITCHEN = 0x18

// Set suspect_id constants
global.SUSPECT_PLUM = 0x20;
global.SUSPECT_SCARLET = 0x21;
global.SUSPECT_GREEN = 0x22;
global.SUSPECT_MUSTARD = 0x23;
global.SUSPECT_WHITE = 0x24;
global.SUSPECT_PEACOCK = 0x25;

// Init room_spr_map
global.room_spr_map[global.ROOM_VERT_HALLWAY] = spr_vert_hallway;
global.room_spr_map[global.ROOM_HORZ_HALLWAY] = spr_horiz_hallway;
for (var i = global.ROOM_STUDY; i <= global.ROOM_KITCHEN; i += 1)
{
	global.room_spr_map[i] = spr_room;
}

// Init suspect_spr_map
global.suspect_spr_map[global.SUSPECT_PLUM] = spr_plum;
global.suspect_spr_map[global.SUSPECT_SCARLET] = spr_scarlet;
global.suspect_spr_map[global.SUSPECT_GREEN] = spr_green;
global.suspect_spr_map[global.SUSPECT_MUSTARD] = spr_mustard;
global.suspect_spr_map[global.SUSPECT_WHITE] = spr_white;
global.suspect_spr_map[global.SUSPECT_PEACOCK] = spr_peacock;

global.ACTION_DISCONNECT = 0x30; // disconnect message
global.ACTION_CONNECT = 0x31;    // connection message
global.ACTION_CHARSEL_REQ = 0x32; // request character
global.ACTION_CHARSEL_ACK = 0x33; // acknowledge character selection
global.ACTION_CHARSEL_NACK = 0x34; // reject character selection
global.ACTION_CHARSEL_STATE_REQ = 0x35; // request/respond with character selection state
global.ACTION_CHARSEL_STATE_RES = 0x36; // request/respond with character selection state
