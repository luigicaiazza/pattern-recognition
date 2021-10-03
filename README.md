<div id="top"></div>

<br />
<div align="center">

  <h3 align="center">pattern-recognition</h3>

</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project was designed and developed as an exercise for [WellD](https://www.welld.ch/).

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To get a local copy of this application up and running follow these simple example steps.

### Prerequisites

Before you begin, ensure you have met the following requirements:
* Java Development Kit (JDK) 1.8 or higher;
* Apache Maven 3.6 or higher.

### Installation

1. Clone the repo
   ```sh
   $ git clone https://github.com/luigicaiazza/pattern-recognition.git
   ```
2. Go to the checked out directory
   ```sh
   $ cd pattern-recognition
   ```
3. Build and install the application
   ```sh
   $ mvn install
   ```
   This will compile the source code of this application and produce an executable jar in `target/patternrecognition-1.0.0.jar`. 

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

To start the application, move to the target directory (i.e., the directory that includes the file `patternrecognition-1.0.0.jar`) and run:
   ```sh
   $ java -jar patternrecognition-1.0.0.jar
   ```
The application package integrates a stand-alone Tomcat web server, running locally on port 8080 (http).

_To get the RESTful Web Services that can be invoked, please refer to the [text of this exercise](http://localhost:8080/) while the application is running._ 

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Luigi Caiazza - lcaiazza88@gmail.com

Project Link: [https://github.com/luigicaiazza/pattern-recognition.git](https://github.com/luigicaiazza/pattern-recognition.git)

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Coursera](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php), from which I got inspired to develop an efficient algorithm to compute the line segments.

<p align="right">(<a href="#top">back to top</a>)</p>

