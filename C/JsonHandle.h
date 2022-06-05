//
// Created by david on 04/06/22.
//

#ifndef C_JSONHANDLE_H
#define C_JSONHANDLE_H

#include <json-c/json.h>

void jsonParserCliente(char msg[]){


    struct json_object *parsed_json;
    struct json_object *score;
    struct json_object *lives;
    struct json_object *lvl;


    size_t n_friends;
    size_t i;

    parsed_json = json_tokener_parse(msg);
    json_object_object_get_ex(parsed_json,"score",&score);
    json_object_object_get_ex(parsed_json,"lives",&lvl);

    printf("Score: %i\n", json_object_get_int(score));
    printf("LVL: %i\n", json_object_get_int(lvl));
};

#endif //C_JSONHANDLE_H
