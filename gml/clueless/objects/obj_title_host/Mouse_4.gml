/// @description Insert description here
// You can write your code in this editor
//server_obj = instance_create_depth(0, 0, 0, );

//var port = get_integer("port", 8000)
var port = 8000; // hardcode for testing

start_service(
    obj_service, 
	network_socket_tcp,
	port,
	5);

ret = connect_to_host(
    obj_client,
	network_socket_tcp, 
	"127.0.0.1",
	port);

//show_message(string(ret));

room_goto_next();