This is an OpenEmbedded layer for the  [etnativ project](https://github.com/laanwj/etna_viv),
an open source reimplementation of Vivante GPU drivers.

Since etnaviv is currently *experimental*, you should not try to use it as a drop-in replacement.
At this stage, this layer is intended to be used for testing purposes on various platforms,
automating the compilation and deployment process.

This layer is made of a common subset in common/, and machine specific .bbappend files (since
the etnaviv build scripts require device specific flags).
