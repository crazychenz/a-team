show_debug_message("DEBUG: Get a character selection.")

var socket_id = async_load[? "id"];
var client_id = -1;
var request_confirmed = false;
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
var suspect_id = buffer_read(buffer, buffer_u8);
show_debug_message("DEBUG: Client " + string(client_id) + " requesting suspect " + string(suspect_id))
show_debug_message("suspect_client_map[" + string(suspect_id) + 
	"] = " + string(suspect_client_map[? suspect_id]))
// Verify request
if (suspect_client_map[? suspect_id] == -1)
{
	// Set new state
	suspect_client_map[? suspect_id] = client_id;
				  
	// Update all clients with suspect map
	var buf = client[? "buffer"]
	buffer_seek(buf, buffer_seek_start, 0);
	buffer_write(buf, buffer_u8, global.ACTION_CHARSEL_ACK);
	buffer_write(buf, buffer_u8, suspect_id);
    network_send_packet(socket_id, buf, buffer_tell(buf));
	show_debug_message("DEBUG: Confirming client " + string(client_id) + " selected suspect " + string(suspect_id))
}
else
{
	// Reject request
	var buf = client[? "buffer"]
	buffer_seek(buf, buffer_seek_start, 0);
	buffer_write(buf, buffer_u8, global.ACTION_CHARSEL_NACK);
    buffer_write(buf, buffer_u8, suspect_id);
    network_send_packet(socket_id, buf, buffer_tell(buf));
	show_debug_message("DEBUG: Rejected client " + string(client_id) + " for suspect " + string(suspect_id))
}