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
  -o, --output=<output>   Output directory for generated images
                            Default: .
  -s, --scale=<scale>     Scale of the output image (1:?)
                            Default: 1
```

For example, if you saved the jar to your server's root directory and want to generate images for all regions, you would use
```
java -jar region-preview.jar world/region/
```

To get smaller images, just add the scale option
```
java -jar region-preview.jar world/region/ -s 2
```
