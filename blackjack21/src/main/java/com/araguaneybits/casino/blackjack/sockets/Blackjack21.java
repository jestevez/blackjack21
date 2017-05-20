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
package com.araguaneybits.casino.blackjack.sockets;

import com.araguaneybits.casino.blackjack.beans.Card;
import com.araguaneybits.casino.blackjack.beans.Deck;
import com.araguaneybits.casino.blackjack.beans.Hand;
import com.araguaneybits.casino.blackjack.beans.Player;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jestevez
 */
public class Blackjack21 implements Serializable {

    private final Logger LOG = LoggerFactory.getLogger(Blackjack21.class);
    private String gameStatus;
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private final Player player;
    private double bet;
    
    public Blackjack21(Player player, double bet) throws Exception {
        this.bet = bet;
        this.player = player;
        if (bet > player.getBalance()) {
            throw new Exception("El monto apostado " + bet + ", es superior a su saldo " + player.getBalance());
        }
        // Comprometer el saldo del cliente
        player.decreaseBalance(bet);
        // Crear una nueva mano
        deck = new Deck();
        // Jugador
        playerHand = new Hand();
        // Crupier
        dealerHand = new Hand();

        // Repartir primeras 4 cartas
        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        playerHand.addCard(deck.dealCard());
        dealerHand.addCard(deck.dealCard());
        gameStatus = "OPEN";
        // Validar si el juego no ha terminado en la primera mano
        if (dealerHand.isBlackjack() && playerHand.isBlackjack()) {
            LOG.info("Hay un empate entre el crupier y el jugador se retorna su apuesta inicial");
            player.increaseBalance(bet);
            gameStatus = "TIE";
        }
        if (playerHand.isBlackjack()) {
            LOG.info("El jugador tiene Blackjack! gana 3 a 2");
            player.increaseBalance(bet * 1.5);
            gameStatus = "BLACKJACK";
        }
        //Validar si el Crupier tiene Blackjack
        if (dealerHand.isBlackjack()) {
            LOG.info("El Crupier tiene Blackjack! el jugador pierde");
            gameStatus = "LOSE";
        }
    }
    
    public void hitDealer() {
        if(dealerHand.getScore() <=21 && dealerHand.getScore() >= playerHand.getScore()) {
            // El crupier gana
            LOG.info("El crupier gana! Crupier: " +dealerHand.getScore() + " Player: "+playerHand.getScore());
            gameStatus = "LOSE";
        }
        else {
            Card card = deck.dealCard();
            LOG.info("La carta del crupier es: " + card);
            dealerHand.addCard(card);
            if(dealerHand.isGameOver()) {
                // El jugador gana
                LOG.info("El jugador gana!");
                gameStatus = "WIN";
                player.increaseBalance(this.bet * 2);
            }
            if(dealerHand.isBlackjack()) {
                // El crupier gana
                LOG.info("El crupier gana! Crupier: " +dealerHand.getScore() + " Player: "+playerHand.getScore());
                gameStatus = "LOSE";
            }
        }
        
    }

    public void hit() {
        Card card = deck.dealCard();
        LOG.info("La carta del jugador es: " + card);
        playerHand.addCard(card);
        if (playerHand.isGameOver()) {
            gameStatus = "LOSE";
        }
        if (playerHand.isBlackjack()) {
            gameStatus = "WIN";
            player.increaseBalance(this.bet * 2);
        }
    }

    public void stand() {
        if ("OPEN".equalsIgnoreCase(gameStatus)) {
            gameStatus = "STAND";
        }
    }

    public void split() {
        // FIXME Tengo que implemetar el SPLIT
    }

    public void doubles(double bet) throws Exception {
        if (bet > player.getBalance()) {
            throw new Exception("No tiene saldo para doblar la apuesta " + bet + " < " + player.getBalance());
        }
        player.decreaseBalance(bet);
        this.bet += bet;
        Card card = deck.dealCard();
        playerHand.addCard(card);
        if (playerHand.isGameOver()) {
            gameStatus = "LOSE";
        }
        if (playerHand.isBlackjack()) {
            gameStatus = "WIN";
            player.increaseBalance(this.bet * 2);
        }
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {        
        if("OPEN".equalsIgnoreCase(gameStatus)) {
            Hand hideHand = new Hand();
            hideHand.addCard(dealerHand.getCards().get(0));
            return hideHand;
        }
        else {
            return dealerHand;
        }
        
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    
}
