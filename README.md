### Alpano
## Introdution
This project is the was done for a course dgiven at the Swiss Technical Institute of Technologies in Lausanne (EPFL) for the course cs108. This course was taught by _Michel Schinz_. For more information about the course and resoucres provided to use for this project visit https://cs108.epfl.ch/archive/17/archive.html .

# Extra features added but not covered on the website

The program has multithreading that will adapt to the number of cores available. There is also the option to save the generated image as a PNG to the hard drive.

## Functionning
This program allows for the creation of panoramas, and annotes them with the name of the summits that are visible on the photo.
The picutres bellow show the panorama rendered by Alpano from the Jura in Switzerland looking south. The colour is determined by the distance that separates the viewer from the terrain, the luninosity depends on the slope of the terrain.


_Generated image_
![The vue of the Alpes from the Jura generated image](https://cs108.epfl.ch/archive/17/p/i/alpano.png "The vue of the Alpes from the Jura")
_Photography of the panorama_
![The vue of the Alpes from the Jura original photograph](https://cs108.epfl.ch/archive/17/p/i/alpes.jpg "The vue of the Alpes from the Jura")

Alpano uses ray tracing to draw the panorama. The rays take into acount the refration of the atmosphere and the curvature of the Earth. The information about the terrain where originally obtained by the **NASA** _Shuttle Radar Topography Mission_. The information was collected by the Space Shuttle Endeavour in Febuary 2000. Fore more information about the mision and collection of the data can be found on [NASA Jet Propulsion Lab website](https://www.jpl.nasa.gov/news/news.php?release=2014-321).

The data used for this programe was downloaded on [Viewfinder panoramas](http://viewfinderpanoramas.org/). The SRTM data from _Viewfinder panoramas_ orginated from the NASA SRTM missions but was manually improved my _Jonathan de Ferranti_.

# Coverage of the SRTM data found in zip file
![](https://cs108.epfl.ch/archive/17/p/i/hgt-area.jpg)

## Instalation

Unzip the _terrainData.zip_ to the root of the folder. After unziping the content of Alpano_v2 should look like.
![](https://raw.githubusercontent.com/Jmion/Alpano_v2/master/hgtFileLocation.JPG)



If you are wanting to run it from the jar then have the * .hgt * files beside the jar. If you run the program and get it generating panoramas that look like a rainbow this indicates that the terrain is flat. That simply means that the HGT files for that location are not available. 

_Please note that simply downloading the SRTM file will not allow you to view the panoramas at that location. There is some code that needs to be changed so that the SRTM files (which are refered to as HGT in this project) are loaded into the app._

Enjoy using the app.

_source: https://cs108.epfl.ch/archive/17/p/00_introduction.html_

