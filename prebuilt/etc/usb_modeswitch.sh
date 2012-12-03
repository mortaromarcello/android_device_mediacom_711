#!/system/bin/busybox sh

#################################################
# usb serial device wrapper
#################################################

# check filename
    
if [ "$1" = "" ]; then
    log "usb_dev_helper: device filename is NULL"
fi

# modem section
if [ -f "$1" ]; then
    PPP0_CONNECTED=`getprop net.ppp0.status`
    PPP1_CONNECTED=`getprop net.ppp1.status`
    if [ "$PPP0_CONNECTED" = "1" ] || [ "$PPP1_CONNECTED" = "1" ]; then
	log "usb_dev_helper: ppp allready connected. skip usb_modeswitch call"
    else
        log "usb_dev_helper: start usb_modeswitch for $1"
	/system/bin/usb_modeswitch -I -c "$1"
    fi
    exit 0;
fi

#################################################
# future need add to this code for BT/GPS 
# and other serial USB devices.
#################################################
log "usb_dev_helper: unknown model device and not supported"
