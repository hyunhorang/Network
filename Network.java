/**
 * Class for network functionality
 * @author Hyun Ho Oh
 */
public class Network {
  
  /**
   * Empty constructor
   */
  public Network() {
    
  }
  
  /**
   * Gets the public IPv4.
   * @return String
   */
  public String getPublicIPv4() {
    String ip = null;
    try {
      URL whatismyip = new URL("http://checkip.amazonaws.com");
      try {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(whatismyip.openStream())
        );
        ip = in.readLine();
        in.close();
      } catch (IOException ex) {
        Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (MalformedURLException ex) {
      Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
    }
    return ip;
  }
  
  /**
   * Gets the name of the 'localhost'.
   * @return 
   */
  public String getLocalHost() {
    try {
      return InetAddress.getLocalHost().toString();
    } catch(UnknownHostException ex) {
      Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }
  
  /**
   * Windows 'netstat' command.
   * @return ArrayList<String>
   */
  public ArrayList<String> netstatE() {
    ArrayList<String> lines = new ArrayList<>();
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("netstat", "-e");
      Process process = processBuilder.start();
      process.waitFor();
      try (
        BufferedReader bufferedReader = 
        new BufferedReader(new InputStreamReader(process.getInputStream()))
      ) {
        String line;
        while((line = bufferedReader.readLine()) != null) {
          lines.add(line);
        }
      }
    } catch(IOException | InterruptedException ex) {
      Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
    return lines;
  }
  
  /**
   * Gets the network byte received for Windows only.
   * @return String
   */
  public String getBytesReceived() {
    for(String line : this.netstatE()) {
      if(line.contains("Bytes")) {
        boolean firstSpaceFound = false;
        int firstSpaceFoundIndex = 0;
        boolean firstBytesFound = false;
        int firstBytesFoundIndex = 0;
        boolean secondSpaceFound = false;
        int secondSpaceFoundIndex = 0;
        for(int i = 0; i < line.length(); i++) {
          if(line.charAt(i) == ' ' && !firstSpaceFound) {
            firstSpaceFound = true;
            firstSpaceFoundIndex = i;
          }
          if(firstSpaceFound && !firstBytesFound) {
            if(line.charAt(i) != ' ') {
              firstBytesFound = true;
              firstBytesFoundIndex = i;
            }
          }
          if(firstBytesFound && !secondSpaceFound) {
            if(line.charAt(i) == ' ') {
              secondSpaceFound = true;
              secondSpaceFoundIndex = i;
            }
          }
        }
        return line.substring(firstBytesFoundIndex, secondSpaceFoundIndex);
      }
    }
    return null;
  }
  
}
