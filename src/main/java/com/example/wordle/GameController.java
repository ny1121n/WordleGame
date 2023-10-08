package com.example.wordle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameController {

    private Word word;

    @GetMapping("/")
    public String index(Model model) {
        if (word == null) {
            word = new Word("plane");
        }

        int attempts = getAttempts(model);
        List<String> results = getResults(model);

        model.addAttribute("word", word.getWord());
        model.addAttribute("attempts", attempts);
        model.addAttribute("results", results);

        return "index";
    }

    @PostMapping("/")
    public String makeGuess(String guess, Model model) {
        int attempts = getAttempts(model);
        List<String> results = getResults(model);

        String result = word.getInfo(guess);
        model.addAttribute("result", result);

        results.add(result); // Add the current result to the list
        model.addAttribute("results", results);

        if (word.isCorrect(guess.toCharArray())) {
            model.addAttribute("message", "Congratulations! You got the answer.");
            resetGame(model);
        } else {
            attempts++;
            model.addAttribute("message", "Try again. Your next guess:");
            model.addAttribute("attempts", attempts);

            if (attempts >= 5) {
                model.addAttribute("message", "Sorry, you've reached the maximum number of attempts.");
                resetGame(model);
            }
        }

        return "index";
    }

    private void resetGame(Model model) {
        word = null; // Reset the game
        model.addAttribute("attempts", 0);
        model.addAttribute("results", new ArrayList<>()); // Reset results
    }

    private int getAttempts(Model model) {
        Integer attempts = (Integer) model.getAttribute("attempts");
        return (attempts != null) ? attempts : 0;
    }

    @SuppressWarnings("unchecked")
    private List<String> getResults(Model model) {
        List<String> results = (List<String>) model.getAttribute("results");
        return (results != null) ? results : new ArrayList<>();
    }
}
