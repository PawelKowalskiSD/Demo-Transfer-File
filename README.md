# Data Transfer Program
Application for transferring large data files between clients

## Table of Contents
* [Introduction](#introduction)
* [Features](#features)
* [Technologies](#technologies)
* [Setup](#setup)

## Introduction
The project was built to transfer large files between clients.


## Features
* creating a file of any size using the GenerateFile.java class
* server to store large files should use the Server.java class
* We add files to the server using the UploadClient.java class, provide the file path in the console
* Downloading files from the server is done using DownloadClient.java, we can select all files that are on the server. in the console, enter the number of the file you want to download


## Technologies
* Gradle


## Setup
To get started with this project, you will need to have the fallowing installed on your local machine:
* JDK 21
* Gradle 8.5

To build and run project, fallow these steps:
* Clone the repository: 'git clone https://github.com/PawelKowalskiSD/Demo-Transfer-File.git'
* Navigate to the project directory: cd Demo-Transfer-File
* Build the project: gradle build
* Run the project: ./gradlew bootRun
