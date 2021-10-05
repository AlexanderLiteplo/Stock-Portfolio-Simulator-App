# Stock-Portfolio-Simulator-App

<img width="1440" alt="Screen Shot 2021-10-04 at 7 51 30 PM" src="https://user-images.githubusercontent.com/65365446/135952692-c9c45f14-ed89-405d-b0b4-b2919344a672.png">

This is a stock tracking app that simulates a real life stock portfolio. In order to allow users to see current stock prices and historical data, I taught myself basic HTML parsing and learned and utilized the Java library jsoup. To accurately store and retrieve the users data, I implemented a persistence system using the Java library JSON. In order to process and analyze the user’s stock portfolio data, I had to create multiple custom data structures such as the overarching “portfolio” data structures that can sort stocks according to four different comparators. 
# Why did I make this?
I created this project as a christmas present for my father. I noticed that he was using an excel spreadsheet to track the stocks over his multiple portfolios in one place. In the excel spreadsheet he would weekly or daily update each individual stock price to the current one in order to calculate his overall return and summarize his stock data. My goal was to allow him to automate this process, and be able to just add his stocks with the quantity and price he bought them add, and have the price update automatically, all while allowing him to see useful statistics about his portfolio.
# What went well?
The project was so fun to make. I got to experience the joy of creating something for other people to use, and combining programming with what was at the time my favourite hobby, stock trading. I made a very clean looking GUI that displays the users stocks clearly, and can order them based on 4 different highly useful parameters. I was able to web scrape real time stock data from Yahoo Finance, and display that data to the user. Finally what I am proud of the most, is the speed in which I was able to create this and figure out web scraping. 
# Shortcomings of Project
This project was very fun to make, however I was not able to create a downloadable desktop app that anyone could use. The app is currently only runnable through an IDE on my computer, because I could not figure out how to make the program able to store and retrieve data without knowing the filepath to the save/load file. In other words I could not get the loading and saving of data into a format such that It could be used on anyones computer. The app also has many flaws, all revolving around the HTML parser. My mistake was that I built all of my code on top of and dependant on the HTML parser that web scraped stock data. This resulted in me not being able to write proper test methods for my classes because I could not enter in my own test values to the classes, as they were completely dependant on getting information from the parser. No tests meant lots of bugs, and the dependancies meant that every time I made change to one area of the app, even if it was to solve a bug, it usually caused another series of bugs.
# What I Learned
- When designing an app only build one function at a time, and test it thoroughly after it is built
- Do not make your classes dependant on each other in such a way that a change to one will break another
- Keep things simple, it is better to make an app that does one useful thing really well, than one hundred things terribly



