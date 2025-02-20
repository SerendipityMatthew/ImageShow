let map;
async function initMap() {
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
    window.AdvancedMarkerElement = AdvancedMarkerElement;

    map = new Map(document.getElementById('map'), {
        center: { lat: 35.8195489, lng: 139.6744804 },
        zoom: 12,
        mapId: 'YOUR_MAP_ID'
    });
}

function createMarkerElement(imageName, imageUrl, size = 16) {
    return new Promise((resolve) => {
        const markerContainer = document.createElement('div');
        markerContainer.className = 'marker-container';
        markerContainer.style.position = 'relative';

        // 创建主圆形容器
        const circleContainer = document.createElement('div');
        circleContainer.className = 'circle-container';
        circleContainer.style.width = `\${size}px`;
        circleContainer.style.height = `\${size}px`;
        circleContainer.style.position = 'relative';

        // 创建圆形背景
        const circle = document.createElement('div');
        circle.className = 'circle';
        circle.style.width = '100%';
        circle.style.height = '100%';
        circle.style.borderRadius = '50%';
        circle.style.border = '2px solid #4285F4';
        circle.style.overflow = 'hidden';
        circle.style.backgroundColor = imageUrl ? 'white' : '#4285F4';
        circle.style.position = 'relative';
        circle.style.zIndex = '1';

        // 创建尖角
        const pointer = document.createElement('div');
        pointer.className = 'pointer';
        pointer.style.position = 'absolute';
        pointer.style.bottom = '-8px';
        pointer.style.left = '50%';
        pointer.style.transform = 'translateX(-50%)';
        pointer.style.width = '16px';
        pointer.style.height = '16px';
        pointer.style.backgroundColor = '#4285F4';
        pointer.style.clipPath = 'polygon(50% 100%, 0 0, 100% 0)';
        pointer.style.zIndex = '0';

        if (imageUrl) {
            const img = new Image();
            img.onload = () => {
                img.style.width = '100%';
                img.style.height = '100%';
                img.style.objectFit = 'cover';
                circle.appendChild(img);

                circleContainer.appendChild(circle);
                circleContainer.appendChild(pointer);
                markerContainer.appendChild(circleContainer);

                markerContainer.style.filter = 'drop-shadow(0 1px 4px rgba(0,0,0,0.3))';

                resolve(markerContainer);
            };
            img.onerror = () => {
                console.error('Failed to load image:', imageUrl);
                circle.style.backgroundColor = '#4285F4';

                circleContainer.appendChild(circle);
                circleContainer.appendChild(pointer);
                markerContainer.appendChild(circleContainer);

                markerContainer.style.filter = 'drop-shadow(0 1px 4px rgba(0,0,0,0.3))';

                resolve(markerContainer);
            };
            img.src = imageUrl;
        } else {
            circleContainer.appendChild(circle);
            circleContainer.appendChild(pointer);
            markerContainer.appendChild(circleContainer);

            markerContainer.style.filter = 'drop-shadow(0 1px 4px rgba(0,0,0,0.3))';

            resolve(markerContainer);
        }
    });
}

async function addMarker(latitude, longitude, imageName, imageUrl) {
    try {
        const position = { lat: latitude, lng: longitude };
        console.log('Adding marker at position:', {
            latitude,
            longitude,
            imageName,
            imageUrl
        });

        const markerElement = await createMarkerElement(imageName, imageUrl);

        const marker = new window.AdvancedMarkerElement({
            position,
            map,
            title: imageName,
            content: markerElement
        });

        // 添加点击效果
        markerElement.addEventListener('mousedown', () => {
            markerElement.style.transform = 'scale(0.95)';
        });

        markerElement.addEventListener('mouseup', () => {
            markerElement.style.transform = 'scale(1)';
        });

        markerElement.addEventListener('mouseleave', () => {
            markerElement.style.transform = 'scale(1)';
        });

        map.setCenter(position);
        return marker;
    } catch (error) {
        console.error('Error adding marker:', error);
        return null;
    }
}

async function addMarkers(locationsJson) {
    try {
        console.log('Received locations JSON:', locationsJson);
        const locations = JSON.parse(locationsJson);
        console.log('Parsed locations:', locations);

        const markers = await Promise.all(
            locations.map(location =>
                addMarker(
                    location.latitude,
                    location.longitude,
                    location.imageName,
                    location.imageUrl
                )
            )
        );

        if (locations.length > 0) {
            map.setCenter({
                lat: locations[0].latitude,
                lng: locations[0].longitude
            });
        }

        return markers.filter(marker => marker !== null);
    } catch (error) {
        console.error('Error adding markers:', error);
        return [];
    }
}

// 添加样式
const style = document.createElement('style');
style.textContent = `
    .marker-container {
        cursor: pointer;
        transition: transform 0.2s, filter 0.2s;
    }
    .marker-container:hover {
        filter: brightness(1.05) drop-shadow(0 1px 4px rgba(0,0,0,0.4)) !important;
    }
    .circle {
        transition: border-color 0.2s;
    }
    .pointer {
        transition: background-color 0.2s;
    }
`;
document.head.appendChild(style);

// 初始化地图
initMap();
