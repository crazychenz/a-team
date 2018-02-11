/// @description Insert description here
// You can write your code in this editor

steps += 1

if (steps % 30 == 0)
{
	count -= 1;
	if (count <= 0)
	{
		room_goto_next();
	}
}

if (steps % 15 == 0)
{
	// Do this every half second (w/ 30 fps)
	// TODO: Do we need to lock this buffer?
	// Fetch the character selection states
	var buf = obj_client.buffer

    buffer_seek(buf, buffer_seek_start, 0);
    buffer_write(buf, buffer_u8, global.ACTION_CHARSEL_STATE_REQ);
    network_send_packet(obj_client.client_socket, buf, buffer_tell(buf));

}