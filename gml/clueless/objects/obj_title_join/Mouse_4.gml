/// @description Insert description here
// You can write your code in this editor

ret = connect_to_host(
    obj_client,
	network_socket_tcp, 
	get_string("ip", "127.0.0.1"),
	get_integer("port", 8000));

//show_message(string(ret));

room_goto_next();