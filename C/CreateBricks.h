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

int * createpower_array(){

    int* test;
    test = malloc(sizeof (int) * 112);
    srand(time(NULL));
    for(int i = 0; i < 112; i++){

        int r = rand() % 5;
        if(r == 0) {
            int r_new = rand() % 6 + 1;

            test[i] = r_new;

        }
        else{
            test[i] = 0;
        }

    }


    return test;
};


#endif //C_CREATEBRICKS_H
