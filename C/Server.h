//
// Created by david on 04/06/22.
//

#ifndef C_SERVER_H
#define C_SERVER_H


char* initbricks();

void nuevoCliente(int servidor, int *clientes, int *nClientes);

int obtenerMaximo(int *tabla, int n);

void compactaClaves(int * tabla, int *n);

#endif //C_SERVER_H
