// connect_to_host(obj, network_socket_tcp, "127.0.0.1", 8000)

var obj = argument[0];

with (obj)
{
    client_type = argument[1];
	dst_ip = argument[2];
	dst_port = argument[3];
	client_socket = network_create_socket(client_type);
	client_connection = network_connect(
	    client_socket, dst_ip, dst_port);
	
	buffer = buffer_create(1024, buffer_fixed, 1);
	
	return client_connection
}

/// @description Insert description here
// You can write your code in this editor
/*
var type = network_socket_tcp
var ip = "A.B.C.D"
var port = 8000
socket = network_create_socket(
  type);
connection = network_connect(
  socket, ip, port);
  
var size = 1024;
var type = buffer_fixed;
var alignment = 1;
buffer = buffer_create(
  size, type, alignment);
*/