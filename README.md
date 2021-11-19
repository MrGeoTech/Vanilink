# Vanilink
Link your Minestom and Vanilla server to get accurate terrain.
# How to use
### How to install
To start using this program, simply download the extension and plugin jars from <a href="https://github.com/MrGeoTech/Vanilink/releases/tag/1.0.0">here</a>. Then, once you have done that, you can add the jars to your respective server folders.
### How to configure
After you start your servers, there are a few things that you have to configure before your servers will become connected.

To configure your servers, start by going into the spiogt plugin folder(`plugins/Vanilink/`) and open up the `config.yml` file. Open up this file and edit the `key` and `port` valuse.

`key` - Think of it as a password that your Minestom server has to know in order to use your Spigot server's resources. Change this to something secure.

`port` - This is the port that the server for connections will be started on. You will have to make sure that this port is forwarded if you have your Minestom server on another computer.

Then, you will have to go to your Minestom server folder and open up the `vanilla-ips.txt` file. Then add the following information:

`key` - The key must be on the first line. This should be only the key, with no other text or information.

`ips` - After the key, you can add as many ips as you want. Follow the format of `<address>:<port>` and only write one address per line.

An example of the spigot config:
```config.yml
port: 20000
key: change_this
```
An example of the Minestom config
```vanilla-ips.txt
change_this
localhost:20000
localhost:20001
```
### How to contribute
Feel free to create a pull request to add your own input to the project. Any and all help is wanted and appreciated. If you have any questions, let me know on discord at MrGeoTech#9470.
### Bug Reports
If you found a bug, please create an issue in github and make sure to provide details on what you did and the errors you got. Make sure to be very clear or we won't be able to help you. If you have any other issues, you can alway contact me on discord at MrGeoTech#9470.
