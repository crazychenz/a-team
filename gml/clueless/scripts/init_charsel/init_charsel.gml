// init_board_location(map, x, y, suspect_id)

var obj;

var board_map = argument[0];
var this_x = argument[1];
var this_y = argument[2];
var suspect_id = argument[3];

// Fetch these globally
var off_x = global.off_x;
var off_y = global.off_y;
var width = global.loc_width;
var height = global.loc_height;

// Add in all the hallways

var final_x = (this_x * width) + off_x;
var final_y = (this_y * height) + off_y;
//show_message(string(suspect_id) + ":" + string(final_x) + "," + string(final_y));
object_set_sprite(obj_location, global.suspect_spr_map[suspect_id]);
obj = instance_create_depth(final_x, final_y, 0, obj_location);
obj.suspect_id = suspect_id;
board_map[this_x, this_y] = obj;
