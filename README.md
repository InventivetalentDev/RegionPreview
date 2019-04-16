# RegionPreview
Utility to create map-like previews of Minecraft region files

![Ingame View](https://yeleha.co/2XaV0KT)

![Preview of the region file](https://media.inventivetalent.org/i/2019/04/15/r.0.-1.mca.png)  


## Usage
Get the latest version from the [releases](https://github.com/InventivetalentDev/RegionPreview/releases) and run it from the commandline
```
Usage: RegionPreview [OPTIONS] [FILES...]
Utility to generated map-like previews of Minecraft region files
      [FILES...]          Files/Directory to generate previews for
      --stitch            Whether to stitch the resulting images together into a
                            large image, instead of outputting single files
      --stitchHeight=<stitchHeight>
                          Number of regions to combine in the z-direction
                            Default: 1
      --stitchStartX=<stitchStartX>
                          Where to start stitching in x-direction
                            Default: 0
      --stitchStartZ=<stitchStartZ>
                          Where to start stitching in z-direction
                            Default: 0
      --stitchWidth=<stitchWidth>
                          Number of regions to combine in the x-direction
                            Default: 1
  -o, --output=<output>   Output directory for generated images
                            Default: .
  -s, --scale=<scale>     Scale of the output image (1:?)
                            Default: 1
```

### Basic

For example, if you saved the jar to your server's root directory and want to generate images for all regions, you would use
```
java -jar region-preview.jar world/region/
```

To get smaller images, just add the scale option
```
java -jar region-preview.jar world/region/ -s 2
``` 


### Large combined image

To stitch together multiple images into one large image, use the --stitch... options, e.g. to create a 4x2 region image starting at -2,-1: 
```
java -jar java -jar region-preview.jar world/region/ -s 2 --stitch --stitchStartX=-2 --stitchStartZ=-1 --stitchWidth=4 --stitchHeight=2
```
