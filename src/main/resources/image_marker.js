let map;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 35.8195489, lng: 139.6744804 },
        zoom: 12
    });

    // 默认添加一个 Marker
    new google.maps.Marker({
        position: { lat: 35.8195489, lng: 139.6744804 },
        map: map,
        title: "Warabi"
    });
}

// JavaScript 方法，用于从 Compose 端接收坐标并添加 Marker
function addMarker(latitude, longitude, imageName, iconBase64) {
    var position = { lat: latitude, lng: longitude};
    console.log('Adding marker at position: latitude = ', latitude, 'longitude =', longitude,'locationName =', imageName, 'iconBase64 =', iconBase64);

    let icon = null;
    if (iconBase64 && iconBase64.trim() !== "") {
        icon = {
            url: 'data:image/png;base64,' + iconBase64,
            scaledSize: new google.maps.Size(50, 50) // 调整图标大小
        };
    }

    new google.maps.Marker({
        position: position,
        map: map,
        title: imageName,
        icon: icon
    });
    map.setCenter(position);
}

   // 批量添加标记方法
function addMarkers(locationsJson) {
    console.log('Adding marker at position: locationsJson = ', locationsJson);

    const locations = JSON.parse(locationsJson);
    console.log('Adding marker at position: locations = ', locations);

    locations.forEach(location => {
        addMarker(
            location.latitude,
            location.longitude,
            location.imageName,
            location.iconBase64
        );
    });

    if (locations.length > 0) {
        map.setCenter({
            lat: locations[0].latitude,
            lng: locations[0].longitude
        });
    }
}
