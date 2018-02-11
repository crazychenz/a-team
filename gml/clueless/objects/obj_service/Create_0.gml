/// @description Insert description here
// You can write your code in this editor
/*var type = network_socket_tcp
var port = 8000
var max_clients = 1
server = network_create_server(type, port, max_clients)
var socket = noone
*/

load_constants();

// local state of who has which characters
suspect_client_map = ds_map_create();
suspect_client_map[? global.SUSPECT_GREEN] = -1;
suspect_client_map[? global.SUSPECT_MUSTARD] = -1;
suspect_client_map[? global.SUSPECT_PEACOCK] = -1;
suspect_client_map[? global.SUSPECT_PLUM] = -1;
suspect_client_map[? global.SUSPECT_SCARLET] = -1;
suspect_client_map[? global.SUSPECT_WHITE] = -1;