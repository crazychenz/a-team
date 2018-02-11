/// @description Insert description here
// You can write your code in this editor
// net event types - 
//   connect, 
//   disconnect, 
//   data

var type_event = async_load[? "type"];

switch (type_event){
    case network_type_connect:
    // Add client to socket
	var socket_assigned = false
	for (var i = 0; i < 5; i += 1)
	{
		if (clients[i] == noone)
		{
			//clients[i] = instance_create_depth(0, 0, 0, obj_client_meta);
			clients[i] = ds_map_create();
			var client = clients[i]
			client[? "socket_id"] = async_load[? "socket"];
			client[? "buffer"] = buffer_create(1024, buffer_fixed, 1);
			
			show_debug_message("DEBUG: Set socket_id to " + string(client[? "socket_id"]) + " for client " + string(i))
			socket_assigned = true;
			break;
		}
	}
	if (socket_assigned == false)
	{
        show_debug_message("DEBUG: Someone tried to connect but no sockets available.")
	}
    break;
  case network_type_disconnect:
      var socket_id = async_load[? "id"];
      for (var i = 0; i < 5; i += 1)
	  {
		  var client = clients[i]
	      if (client[? "socket_id"] == socket_id)
	      {
			  // TODO: Free the client[? "buffer"]
              clients[i] = noone;
              break;
		  }
	  }
      show_debug_message("DEBUG: Someone disconnected and we freed the socket.")
      break;
  case network_type_data:

	  var buffer = async_load[? "buffer"];
      buffer_seek(buffer, buffer_seek_start, 0)
      var msg_id = buffer_read(buffer, buffer_u8);

	  switch (msg_id)
	  {
		  case global.ACTION_CHARSEL_STATE_REQ:
		      action_charsel_state_req(buffer);
		  	  break;

		  case global.ACTION_CHARSEL_REQ:
		      
			  
			  
			  
			  break;
	  }
    break;
}
