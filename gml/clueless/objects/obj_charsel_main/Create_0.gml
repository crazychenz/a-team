/// @description Insert description here
// You can write your code in this editor

load_constants();

steps = 0;
count = 5; // ten seconds for testing
//count = 60; // one minute

// TODO: Fetch these globally
var off_x = 64;
var off_y = 64;
var width = 64;
var height = 64;

// Add in all the hallways
// TODO: Clean up magic numbers (20 is first suspect, 25 is last suspect)
var suspect_selection;
for (var i = global.SUSPECT_PLUM; i <= global.SUSPECT_PEACOCK; i += 1)
{
	// Spread the characters across the screen
    var final_x = ((i - global.SUSPECT_PLUM) * width * 2) + off_x;
    var final_y = 128 + off_y;
    object_set_sprite(obj_charsel, global.suspect_spr_map[i]);
    obj = instance_create_depth(final_x, final_y, 0, obj_charsel);
    obj.suspect_id = i;
    suspect_selection[i - 20] = obj;
}