var in_buffer = argument[0]

//show_debug_message("DEBUG: Responding with charsel state")
var socket_id = async_load[? "id"];
var client_id = -1;
var client = noone;
for (var i = 0; i < 5; i += 1)
{
	client = clients[i]
	if (client != noone && client[? "socket_id"] == socket_id)
	{
		client_id = i;
		break;
	}
}
if (client != noone)
{
var buf = client[? "buffer"]
buffer_seek(buf, buffer_seek_start, 0);
buffer_write(buf, buffer_u8, global.ACTION_CHARSEL_STATE_RES);
for (var i = global.SUSPECT_PLUM; i <= global.SUSPECT_PEACOCK; i += 1)
{
	buffer_write(buf, buffer_u8, i);
	buffer_write(buf, buffer_s8, suspect_client_map[? i]);
}
//show_debug_message("Sending state info " + string(buffer_tell(buf)))
show_debug_message(teststr)
network_send_packet(socket_id, buf, buffer_tell(buf));
				
}