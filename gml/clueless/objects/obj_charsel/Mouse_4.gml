/// @description Insert description here
// You can write your code in this editor

// Send character selection
var buf = obj_client.buffer

buffer_seek(buf, buffer_seek_start, 0);
buffer_write(buf, buffer_u8, global.ACTION_CHARSEL_REQ);
buffer_write(buf, buffer_u8, suspect_id);
network_send_packet(obj_client.client_socket, buf, buffer_tell(buf));

//room_goto_next();