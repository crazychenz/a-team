var buffer = argument[0]
var message_id = 
  buffer_read(buffer, buffer_u8);
  
switch (message_id) {
  case 1:
    var mx = buffer_read(buffer, buffer_u32);
    var my = buffer_read(buffer, buffer_u32);
	instance_create_depth(mx, my, 0, obj_click)
	break;
}