/**
 * This is a bit hacky, but will work to retrieve the ip of the client in the local network.
 * See https://stackoverflow.com/questions/391979/how-to-get-clients-ip-address-using-javascript/32841164#32841164
 * @param {*} onNewIP
 */
export const findIP = onNewIP => {
  //  onNewIp - your listener function for new IPs
  let myPeerConnection =
    window.RTCPeerConnection ||
    window.mozRTCPeerConnection ||
    window.webkitRTCPeerConnection; //compatibility for firefox and chrome
  let pc = new myPeerConnection({ iceServers: [] }),
    noop = function() {},
    localIPs = {},
    ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g;

  const ipIterate = ip => {
    if (!localIPs[ip]) {
      localIPs[ip] = true;
      onNewIP(ip, localIPs);
    }
  };
  pc.createDataChannel(""); //create a bogus data channel
  pc.createOffer(function(sdp) {
    sdp.sdp.split("\n").forEach(function(line) {
      if (line.indexOf("candidate") < 0) return;
      line.match(ipRegex).forEach(ipIterate);
    });
    pc.setLocalDescription(sdp, noop, noop);
  }, noop); // create offer and set local description
  pc.onicecandidate = function(ice) {
    //listen for candidate events
    if (
      !ice ||
      !ice.candidate ||
      !ice.candidate.candidate ||
      !ice.candidate.candidate.match(ipRegex)
    )
      return;
    ice.candidate.candidate.match(ipRegex).forEach(ipIterate);
  };
};
