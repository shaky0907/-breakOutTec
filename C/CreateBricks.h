//
// Created by david on 05/06/22.
//

#ifndef C_CREATEBRICKS_H
#define C_CREATEBRICKS_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include "Constantes.h"

char * createpower_array(){

    char* test;
    test = malloc(sizeof (char) * 112);
    srand(time(NULL));
    for(int i = 0; i < 112; i++){
        printf(" brick: %d\n",i);
        int r = rand() % 10;
        if(r == 0) {
            int r_new = rand() % 6 + 1;

            int length = snprintf(NULL, 0, "%d", r_new);
            char *str = malloc(length + 1);
            snprintf(str, length + 1, "%d", r_new);

            strcat(test,str);
            printf("Poder: %c\n",test[i]);
        }
        else{
            strcat(test,"0");
        }

    }
    return test;
};


#endif //C_CREATEBRICKS_H
