/*
 * Copyright 2017 AraguaneyBits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.araguaneybits.casino.blackjack.main;

import com.araguaneybits.casino.blackjack.beans.Card;
import com.araguaneybits.casino.blackjack.beans.Deck;
import com.araguaneybits.casino.blackjack.beans.Hand;
import com.araguaneybits.casino.blackjack.beans.Player;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author jestevez
 */
public class Blackjack21 {

    public static void main(String[] args) throws IOException {
        // Iniciar el Juego
        System.out.println("Bienvenido a Blackjack21!");
        Scanner input = new Scanner(System.in);
        // Jugador        
        Player player = new Player("Yeyo", 100);
        // Crupier        
        Player dealer = new Player("Dealer", 0);
        while (true) {
            boolean loser = false;
            boolean winner = false;
            System.out.println("Su saldo es : " + player.getBalance());
            System.out.print("Ingrese el valor de su apuesta: ");
            double bet = input.nextDouble();
            if (bet > player.getBalance()) {
                System.out.printf("El monto apostado %d, es superior a su saldo %d\n", bet, player.getBalance());
                continue;
            }
            // Comprometer el saldo del cliente
            player.decreaseBalance(bet);
            // Crear una nueva mano
            Deck deck = new Deck();
            // Jugador
            Hand playerHand = new Hand();
            // Crupier
            Hand dealerHand = new Hand();

            // Repartir primeras 4 cartas
            playerHand.addCard(deck.dealCard());
            dealerHand.addCard(deck.dealCard());
            playerHand.addCard(deck.dealCard());
            dealerHand.addCard(deck.dealCard());
            System.out.println("La carta mostrada del Crupier es : " + dealerHand.getCards().get(0));
            System.out.println("Tu primera carta es: " + playerHand.getCards().get(0));
            System.out.println("Tu segunda carta es: " + playerHand.getCards().get(1));

            // Validar el empate
            if (dealerHand.isBlackjack() && playerHand.isBlackjack()) {
                System.out.println("Hay un empate entre el crupier y el jugador se retorna su apuesta inicial");
                player.increaseBalance(bet);
                continue;
            }
            if (playerHand.isBlackjack()) {
                System.out.println("El jugador tiene Blackjack! gana 3 a 2");
                player.increaseBalance(bet * 1.5);
                continue;
            }
            //Validar si el Crupier tiene Blackjack
            if (dealerHand.isBlackjack()) {
                System.out.println("El Crupier tiene Blackjack! el jugador pierde");
                continue;
            }

            // Opciones del Jugador
            while (true) {
                System.out.println("Tienes: " + playerHand.getScore());
                System.out.println("Ingrese una opcion Hit(1), Stand (2), Double (3), Split (4)");
                int option = input.nextInt();
                System.out.println("option " + option);

                if (option == 1) {
                    Card card = deck.dealCard();
                    System.out.println("La carta del jugador es: " + card);
                    playerHand.addCard(card);
                    if (playerHand.isGameOver()) {
                        loser = true;
                        break;
                    }
                    if (playerHand.isBlackjack()) {
                        winner = true;
                        break;
                    }
                } else if (option == 2) {
                    if (playerHand.getScore() <= 17) {
                        // FIXME No se puede quedar en menos de 16?
                    }
                    break;
                } else if (option == 3) {
                    player.decreaseBalance(bet);
                    bet += bet;
                    Card card = deck.dealCard();
                    playerHand.addCard(card);
                    System.out.println("Apuesta  "+bet);
                    if (playerHand.isGameOver()) {
                        loser = true;
                        break;
                    }
                    if (playerHand.isBlackjack()) {
                        winner = true;
                        break;
                    }
                } else if (option == 4) {
                    System.out.println("Proximamente!");
                } else {
                    System.out.println("Operación no valida!");
                }
            }
            // 
            System.out.println("La segunda carta del Crupier es : " + dealerHand.getCards().get(1));
            
            if (loser) {
                System.out.println("Juego terminado su puntuacion se excede " + playerHand.getScore());
            } else if (winner) {
                System.out.println("El jugador tiene Blackjack!");
                player.increaseBalance(bet * 2);
            } else {
                // Si el jugador no pide mas cartas y tiene mas de 17
                // El crupier esta obligado a pedir mas cartas
                while (true) { // dealerHand.getScore() < 17
                    Card card = deck.dealCard();
                    System.out.println("La carta del crupier es: " + card);
                    dealerHand.addCard(card);
                    if(dealerHand.isGameOver()) {
                        // El jugador gana
                        System.out.println("El jugador gana!");
                        player.increaseBalance(bet * 2);
                        break;
                    }
                    if(dealerHand.isBlackjack() || dealerHand.getScore() > playerHand.getScore()) {
                        // El crupier gana
                        System.out.println("El crupier gana! Crupier: " +dealerHand.getScore() + " Player: "+playerHand.getScore());
                        break;
                    }
                }
            }
            System.out.println("¿Volver a jugar? Si (S) o No (N)");
            String keep = input.next();
            if("N".equalsIgnoreCase(keep)) {
                System.out.println("Gracias por jugar!");
                break;
            }
        }
    }

}
