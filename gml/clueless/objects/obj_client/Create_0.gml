load_constants();

suspect_id = -1;

// local state of who has which characters
suspect_client_map = ds_map_create();
suspect_client_map[? global.SUSPECT_GREEN] = -1;
suspect_client_map[? global.SUSPECT_MUSTARD] = -1;
suspect_client_map[? global.SUSPECT_PEACOCK] = -1;
suspect_client_map[? global.SUSPECT_PLUM] = -1;
suspect_client_map[? global.SUSPECT_SCARLET] = -1;
suspect_client_map[? global.SUSPECT_WHITE] = -1;