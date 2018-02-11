/// @description Insert description here
// You can write your code in this editor
instance_create_depth(mouse_x, mouse_y, 0, obj_click)
buffer_seek(buffer, buffer_Seek_Start, 0)
buffer_write(buffer, buffer_u8, 1);
buffer_write(buffer, buffer_u32, mouse_x)
buffer_write(buffer, buffer_u32, mouse_y)
network_send_packet(socket, buffer, buffer_tell(buffer));