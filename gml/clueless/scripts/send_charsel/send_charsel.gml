/// @description Insert description here
// You can write your code in this editor

// send_charsel(suspect_id)

var buf = obj_client.buffer

buffer_seek(buf, buffer_seek_start, 0);
buffer_write(buf, buffer_u8, global.ACTION_CHARSEL);
buffer_write(buf, buffer_u32, argument[0]);
network_send_packet(obj_client.client_socket, buf, buffer_tell(buf));
