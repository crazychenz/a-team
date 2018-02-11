/// @description Insert description here
// You can write your code in this editor
/// @description Insert description here
// You can write your code in this editor
//show_message(parameter_string(0))

load_constants();



// Initialize empty board map.
for (var map_y = 0; map_y < 7; map_y += 1)
{
  for (var map_x = 0; map_x < 7; map_x += 1)
  {
	  board_map[map_x, map_y] = noone;
  }
}

init_board_location(board_map, 1, 1, global.ROOM_STUDY);
init_board_location(board_map, 2, 1, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 3, 1, global.ROOM_HALL);
init_board_location(board_map, 4, 1, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 5, 1, global.ROOM_LOUNGE);

init_board_location(board_map, 1, 2, global.ROOM_VERT_HALLWAY);
init_board_location(board_map, 3, 2, global.ROOM_VERT_HALLWAY);
init_board_location(board_map, 5, 2, global.ROOM_VERT_HALLWAY);

init_board_location(board_map, 1, 3, global.ROOM_LIBRARY);
init_board_location(board_map, 2, 3, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 3, 3, global.ROOM_BILLIARDS);
init_board_location(board_map, 4, 3, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 5, 3, global.ROOM_DINNING);

init_board_location(board_map, 1, 4, global.ROOM_VERT_HALLWAY);
init_board_location(board_map, 3, 4, global.ROOM_VERT_HALLWAY);
init_board_location(board_map, 5, 4, global.ROOM_VERT_HALLWAY);

init_board_location(board_map, 1, 5, global.ROOM_CONSERVATORY);
init_board_location(board_map, 2, 5, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 3, 5, global.ROOM_BALLROOM);
init_board_location(board_map, 4, 5, global.ROOM_HORZ_HALLWAY);
init_board_location(board_map, 5, 5, global.ROOM_KITCHEN);

suspect_location_by_id = ds_map_create();
init_suspect(board_map, suspect_location_by_id, 4, 0, global.SUSPECT_SCARLET);
init_suspect(board_map, suspect_location_by_id, 0, 2, global.SUSPECT_MUSTARD);
init_suspect(board_map, suspect_location_by_id, 6, 2, global.SUSPECT_WHITE);
init_suspect(board_map, suspect_location_by_id, 0, 4, global.SUSPECT_GREEN);
init_suspect(board_map, suspect_location_by_id, 2, 6, global.SUSPECT_PEACOCK);
init_suspect(board_map, suspect_location_by_id, 4, 6, global.SUSPECT_PLUM);




//show_debug_message("CLIENT: Our suspect is at " + 
//string(location[? "x"]) + "," + string(location[? "y"]))








