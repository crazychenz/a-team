// init_board_location(map, x, y, room_id)

var obj;

board_map = argument[0]
var this_x = argument[1]
var this_y = argument[2]
var room_id = argument[3]

// Fetch these globally
var off_x = global.off_x
var off_y = global.off_y
var width = global.loc_width
var height = global.loc_height

// Add in all the hallways

var final_x = (this_x * width) + off_x
var final_y = (this_y * height) + off_y
object_set_sprite(obj_location, global.room_spr_map[room_id]);
obj = instance_create_depth(final_x, final_y, 0, obj_location);
obj.room_id = room_id;
obj.this_x = this_x;
obj.this_y = this_y;
board_map[this_x, this_y] = obj
