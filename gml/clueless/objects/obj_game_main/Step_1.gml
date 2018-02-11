/// @description Insert description here
// You can write your code in this editor

// Determine possible moves
our_suspect = obj_client.suspect_id;
var location = suspect_location_by_id[? our_suspect];
our_x = location[? "x"]
our_y = location[? "y"]

has_moves = false;
for (var mapy = 0; mapy < 7; mapy += 1)
{
	for (var mapx = 0; mapx < 7; mapx += 1)
	{
		if (board_map[mapx, mapy] != noone)
		{
		    board_map[mapx, mapy].possible_move = noone;
		}
	}
}

// Try North
if (our_y > 1)
{
	this_pos = board_map[our_x, our_y - 1];
	if (this_pos != noone && this_pos.suspect_id == -1)
	{
		this_pos.possible_move = true;
		has_moves = true;
	}
}

// Try South
if (our_y < 6)
{
	this_pos = obj_game_main.board_map[our_x, our_y + 1];
	if (this_pos != noone && this_pos.suspect_id == -1)
	{
		this_pos.possible_move = true;
		has_moves = true;
	}
}

// Try East
if (our_x < 6)
{
	this_pos = obj_game_main.board_map[our_x + 1, our_y];
	if (this_pos != noone && this_pos.suspect_id == -1)
	{
		this_pos.possible_move = true;
		has_moves = true;
	}
}

// Try West
if (our_x > 1)
{
	this_pos = obj_game_main.board_map[our_x - 1, our_y];
	if (this_pos != noone && this_pos.suspect_id == -1)
	{
		this_pos.possible_move = true;
		has_moves = true;
	}
}

if (!has_moves)
{
    //show_debug_message("CLIENT: Can only suggest or accuse this turn.")
}