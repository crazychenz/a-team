/// @description Insert description here
// You can write your code in this editor



draw_set_colour(c_white);
draw_text(20, 20, string(count));

var state = "";
for (var i = global.SUSPECT_PLUM; i <= global.SUSPECT_PEACOCK; i+=1)
{
    state += string(i) + " " + string(obj_client.suspect_client_map[? i]) + ", "
}

draw_text(45, 45, state)