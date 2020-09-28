# Alpano
_If you are simply wanting to use this app skip to the instalation part of this guide_
## Introdution
This project is the was done for a course given at the Swiss Technical Institute of Technologies in Lausanne (EPFL) for the course cs108. This course was taught by _Michel Schinz_. For more information about the course and resoucrces provided to us for this project visit https://cs108.epfl.ch/archive/17/archive.html .

##### Extra features added but not covered on the website

The program has multithreading that will adapt to the number of cores available on the machine. There is also the option to save the generated image as a PNG to the hard drive. There is also a list of Predefined Panoramas that enter information into the program to generate nice panoramas within the bounds of the SRTM (_see bellow_) data available.

## Functionning
This program allows for the creation of panoramas, and annotes them with the name of the summits that are visible on the photo.
The picutres bellow show the panorama rendered by Alpano from the Jura in Switzerland looking south. The colour is determined by the distance that separates the viewer from the terrain, the luninosity depends on the slope of the terrain.


_Generated image_
![The vue of the Alpes from the Jura generated image](https://cs108.epfl.ch/archive/17/p/i/alpano.png "The vue of the Alpes from the Jura")
_Photography of the panorama_
![The vue of the Alpes from the Jura original photograph](https://cs108.epfl.ch/archive/17/p/i/alpes.jpg "The vue of the Alpes from the Jura")

Alpano uses ray tracing to draw the panorama. The rays take into acount the refration of the atmosphere and the curvature of the Earth. The information about the terrain where originally obtained by the **NASA** _Shuttle Radar Topography Mission_. The information was collected by the Space Shuttle Endeavour in Febuary 2000. Fore more information about the mision and collection of the data can be found on [NASA Jet Propulsion Lab website](https://www.jpl.nasa.gov/news/news.php?release=2014-321).

The data used for this programe was downloaded on [Viewfinder panoramas](http://viewfinderpanoramas.org/). The SRTM data from _Viewfinder panoramas_ orginated from the NASA SRTM missions but was manually improved by _Jonathan de Ferranti_.

### Coverage of the SRTM data found in zip file
![](https://cs108.epfl.ch/archive/17/p/i/hgt-area.jpg)

## Instalation
After having read about SRTM and the data that was collected we are now going to import the data into the prorgam. This step needs to be done manually since the files are to large to simply append to this project in Github. The data collected about the elevation of the terrain is stored in files wiht the **.hgt** file extension.

* download the 2 zip files:

   [ZIP 1 of SRTM DATA](https://keybase.pub/jmion/Alpano%20resoucres/terrainData.zip.001)  
   [ZIP 2 od SRTM DATA](https://keybase.pub/jmion/Alpano%20resoucres/terrainData.zip.002)    
Unzip the _terrainData.zip_ to the root of the folder. After unziping the content of Alpano_v2 should look like.
![](https://raw.githubusercontent.com/Jmion/Alpano_v2/master/hgtFileLocation.JPG)



If you are wanting to run it from the jar then have the * .hgt * files beside the jar. If you run the program and get it generating panoramas that look like a rainbow this indicates that the terrain is flat. That simply means that the HGT files for that location are not available. 

_Please note that simply downloading the SRTM file will not allow you to view the panoramas at that location. There is some code that needs to be changed so that the SRTM files (which are refered to as HGT in this project) are loaded into the app._

#### I few more picutres
_Here is a 40000x10000 pixel 69.5 MB picture that was generated on a intel xeon 6 core with 24 GB of RAM. Rendering time took over 240 seconds. This is outside what the program will allow you to generate. Launching the generation of such a panorama from a seperate main methode is required since realisitically not a lot of people have 24 GB of RAM to allow for the calculation of such panoramas. The JRE had to get passed the required parameters to allow the 24GB of RAM. Lack to do so will yield an outOfMemoryException._

_If the image bellow refuses to load you can get it at_ https://jmion.keybase.pub/Alpano%20resoucres/niesen-color1.png
![](https://jmion.keybase.pub/Alpano%20resoucres/niesen-color1.png)

Large image should be above this.

Here is the panorama of the preset Alp Jura.
![](https://jmion.keybase.pub/Alpano%20resoucres/jura.png)

Here is the panorama of Finsterrarhorn.
![](https://jmion.keybase.pub/Alpano%20resoucres/Finsterrarhorn.png)

If Alpano generates and image like the one bellow, this indicates that the SRTM data is missing. Alpano will assume that the terrain outside of where the SRTM data is is flat and has an altitude of 0 meters above see level.
![](https://github.com/Jmion/Alpano_v2/blob/master/rainbowPanorama.png?raw=true)


_source: https://cs108.epfl.ch/archive/17/p/00_introduction.html_

