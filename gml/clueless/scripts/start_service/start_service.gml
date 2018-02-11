var obj = argument[0];

with (obj)
{
    server_type = argument[1];
    server_port = argument[2];
    server_max_clients = argument[3];
    for (var i = 0; i < 5; i += 1)
    {
	    clients[i] = noone;
    }

    server_socket = network_create_server(
	    server_type, server_port, server_max_clients);

}
