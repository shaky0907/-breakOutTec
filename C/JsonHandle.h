//
// Created by david on 04/06/22.
//

#ifndef C_JSONHANDLE_H
#define C_JSONHANDLE_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <json-c/json.h>



char* int_to_str(int num){

    int length = snprintf(NULL, 0, "%d", num);
    char *str = malloc(length + 1);
    snprintf(str, length + 1, "%d", num);
    return str;
};
void jsonParserCliente(char msg[]){


    struct json_object *parsed_json;
    struct json_object *score;
    struct json_object *lives;
    struct json_object *lvl;


    size_t n_friends;
    size_t i;

    parsed_json = json_tokener_parse(msg);
    json_object_object_get_ex(parsed_json,"scores",&score);
    json_object_object_get_ex(parsed_json,"power",&lvl);

    printf("Scores: %s\n", json_object_get_string(score));
    printf("powers: %s\n", json_object_get_string(lvl));
};
char* generateJSONINIT(int score[],int power[]){


    char* json;
    json = malloc(sizeof (char)*200);


    /// formateo del json

    ///score
    strcat(json,"{""\"scores\":[");

    for(int i = 0; i<3;i++){
        char* tmp = int_to_str(score[i]);
        strcat(json,tmp);
        strcat(json,",");
    }
    char* tmp1 = int_to_str(score[3]);
    strcat(json,tmp1);
    strcat(json,"],");

    ///powers
    printf("2: %s\n", json);
    strcat(json,"\"power\":[");

    for(int j = 0; j < 111;j++){
        char* tmp3 = int_to_str(power[j]);
        strcat(tmp3,",");
        printf("Power: %s\n", tmp3);
        strcat(json,tmp3);
        free(tmp3);

    }
    printf("3: %s\n", json);
    char* tmp2 = int_to_str(power[111]);
    strcat(json,tmp2);

    strcat(json,"]");

    printf("4: %s\n", json);
    strcat(json,"}");

    printf("FINAL JSON %s\n", json);


    return json;
};

#endif //C_JSONHANDLE_H
