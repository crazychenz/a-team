/// @description Insert description here
// You can write your code in this editor

var type_event = async_load[? "type"];

switch (type_event){
	case network_type_data:
	  var buffer = async_load[? "buffer"];
      buffer_seek(buffer, buffer_seek_start, 0)
      var msg_id = buffer_read(buffer, buffer_u8);
	  switch (msg_id)
	  {
		  case global.ACTION_CHARSEL_STATE_RES:
			  for (var i = global.SUSPECT_PLUM; i <= global.SUSPECT_PEACOCK; i += 1)
			  {
			      var sus_id = buffer_read(buffer, buffer_u8);
		  		  var client_id = buffer_read(buffer, buffer_s8);
				  suspect_client_map[? sus_id] = client_id;
	  		  }
			  break;
          case global.ACTION_CHARSEL_ACK:
		      show_debug_message("CLIENT: Received charsel ACK");
			  suspect_id = buffer_read(buffer, buffer_u8);
			  break;
	  }
	  break;
}
