# iBroadcast music synchroniser 

## Summary

The Java application is designed to synchronize a local music library with your iBroadcast library, ensuring that both libraries stay updated and consistent. The application provides a seamless and efficient way to manage your music collection.

### Key Features

1. **Local and Remote Library Integration:** The application allows users to connect and synchronize their local music library with a remote library stored on [iBroadcast](https://www.ibroadcast.com/).

2. **File Comparison and Transfer:** The application employs intelligent file comparison algorithms to detect differences between the local and remote libraries. It identifies new tracks, modified metadata, or deleted files, and efficiently transfers the necessary files from your local collection to [iBroadcast](https://www.ibroadcast.com/).

3. **Metadata Harmonization:** The application synchronizes metadata such as song titles, artists, albums, and genres, ensuring that the information remains consistent between the local and remote libraries. This feature enhances the user experience and facilitates accurate organization and search capabilities.

4. **Scheduling and Automation:** Users can configure synchronization schedules according to their preferences. 

5. **Commandline Interface:** The application provides a commandline interface allowing ultimate flexibility. 

---

Overall, this provides a robust solution for synchronizing [iBroadcast](https://www.ibroadcast.com/) with your local library music library. 

## Commandline

```
Usage: ibsync
  -b, --database=<database>
                      Directory for storing the database file
  -c, --clean         Moves tracks on iBroadcast to the trash folder that are
                        not stored locally
  -d, --dry           Performs a dry-run, does not make any changes
  -h, --help          Print this message
  -i, --input=<inputFolder>
                      The folder containing the music library to synchronise
  -p, --password=<password>
                      Your ibroadcast password
  -s, --send          Sends any missing or updated music from the local
                        filesystem to iBroadcast
  -u, --user=<user>   Your ibroadcast username

```
