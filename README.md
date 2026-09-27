# Beyer Hack Club Bot
A Discord Bot that provides a variety of utilities for our club.

![BHC Bot Demo Screenshot](https://cdn.hackclub.com/01a0e3f4-148e-73d6-99f1-609cfd768aac/bhc-bot-demo.jpg)

## Try it!
Setup is pretty easy as a result of the included a docker file! 
- Clone the repo
- Create a .env file with "AUTHORIZATION" for the BHC API, "APIKEY" for the BHC Git instance, and "TOKEN" as the bot token.
  - Note that URLs and names are hard coded, this bot isn't intended to be used by non Beyer Hack Club members, though feel free to remix it. 
- Run `docker compose up`.

## Features
- Automatic account creation for BHC's Git instance
- Information Command (/info & /donate)
- Easily update the [website](https://beyerhack.club) using our [API](https://git.beyerhack.club/max/bhc-api).
- Read, write, and manage member info through JSON files
- Manage and share passwords
- Surveys

Made using JDA (Java Discord API)
