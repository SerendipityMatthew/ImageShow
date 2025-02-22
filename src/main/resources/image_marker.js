let map;
async function initMap() {
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");
    window.AdvancedMarkerElement = AdvancedMarkerElement;

    map = new Map(document.getElementById('map'), {
        center: { lat: 35.8195489, lng: 139.6744804 },
        zoom: 17,
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

async function addMarkerAnimation(latitude, longitude, imageName, imageUrl, shouldCenter = false) {
    try {
        const position = { lat: latitude, lng: longitude };
        console.log('Adding marker at position:', { latitude, longitude, imageName, imageUrl });

        const markerElement = await createMarkerElement(imageName, imageUrl);

        // 初始状态（透明 & 上移）
        markerElement.style.opacity = "0";
        markerElement.style.transform = "translateY(-50px) scale(0.8)";

        const marker = new window.AdvancedMarkerElement({
            position,
            map,
            title: imageName,
            content: markerElement
        });

        // 仅在第一个标记时设置地图中心
        if (shouldCenter) {
            map.setCenter(position);
        }

        // 执行动画：从上掉落
        setTimeout(() => {
            markerElement.style.opacity = "1";
            markerElement.style.transform = "translateY(0) scale(1)";
        }, 100); // 100ms 后开始动画

        return marker;
    } catch (error) {
        console.error('Error adding marker:', error);
        return null;
    }
}


async function addMarkersAnimation(locationsJson) {
    try {
        console.log('Received locations JSON:', locationsJson);
        const locations = JSON.parse(locationsJson);
        console.log('Parsed locations:', locations);

        const markers = [];

        for (let i = 0; i < locations.length; i++) {

            setTimeout(async () => {
                const shouldCenter = i === 0; // 只在第一个标记时居中
                const marker = await addMarker(
                    locations[i].latitude,
                    locations[i].longitude,
                    locations[i].imageName,
                    locations[i].imageUrl,
                    i == 0,
                    i % 20 == 0
                );

                if (marker) {
                    markers.push(marker);
                }
            }, i * 300); // 每个标记间隔 300ms
        }

        return markers;
    } catch (error) {
        console.error('Error adding markers:', error);
        return [];
    }
}

let isPaused = false;   // 控制是否暂停

async function addMarkers(locationsJson) {
    try {
        console.log('Received locations JSON:', locationsJson);
        const locations = JSON.parse(locationsJson);
        console.log('Parsed locations:', locations);

        // **清除所有 marker**
        clearMarkers();

        const markers = [];

        for (let i = 0; i < locations.length; i++) {
            // **检查是否暂停，暂停时等待恢复**
            await waitForResume();

            const location = locations[i];

            if (i === 0) {
                // **第一个 marker 设置中心点**
                map.setCenter({ lat: location.latitude, lng: location.longitude });

                // **等待 3 秒**
                await new Promise(resolve => setTimeout(resolve, 3000));
            }

            // **添加 marker**
            const marker = await addMarker(
                location.latitude,
                location.longitude,
                location.imageName,
                location.imageUrl,
                i === 0
            );

            if (marker) {
                markers.push(marker);
                markersArray.push(marker); // 存储 marker，方便清除
            }

            // **500ms 间隔，依次添加**
            if (i > 0) {
                await new Promise(resolve => setTimeout(resolve, 500));
            }
        }

        return markers;
    } catch (error) {
        console.error('Error adding markers:', error);
        return [];
    }
}

// **暂停添加 marker**
function pauseMarkers() {
    isPaused = true;
    console.log("Marker adding paused.");
    if (window.onMarkerPaused) window.onMarkerPaused();

}

// **恢复添加 marker**
function resumeMarkers() {
    isPaused = false;
    console.log("Marker adding resumed.");
}

// **等待恢复**
function waitForResume() {
    return new Promise(resolve => {
        const checkResume = setInterval(() => {
            if (!isPaused) {
                clearInterval(checkResume);
                resolve();
            }
        }, 100);
    });
}



async function addMarker(latitude, longitude, imageName, imageUrl, shouldCenter = false, isMoveMap = false) {
    try {
        // 生成地图的新中心点（增加微小偏移）
        const latOffset = (Math.random() - 0.5) * 0.002;  // 纬度偏移 ±0.001 ~ ±0.002
        const lngOffset = (Math.random() - 0.5) * 0.002;  // 经度偏移 ±0.001 ~ ±0.002

        const position = { lat: latitude, lng: longitude };
        const newCenter = { lat: latitude + latOffset, lng: longitude + lngOffset };

        console.log('Adding marker at position:', { latitude, longitude, latOffset, lngOffset });

        const markerElement = await createMarkerElement(imageName, imageUrl);

        // 初始状态（透明 & 上移）
        markerElement.style.opacity = "0";
        markerElement.style.transform = "translateY(-50px) scale(0.8)";

        const marker = new window.AdvancedMarkerElement({
            position,
            map,
            title: imageName,
            content: markerElement
        });


        // **如果是第一个标记，直接 setCenter**
        if (shouldCenter) {
            map.setCenter(position);
        } else {
            if (isMoveMap) {
                // **后续标记，平移地图**
                const panX = (Math.random() - 0.5) * 80;  // X 偏移 ±40 ~ ±80 像素
                const panY = (Math.random() - 0.5) * 80;  // Y 偏移 ±40 ~ ±80 像素
                setTimeout(() => {
                    map.panBy(panX, panY);
                }, 300);
            }

        }



        // 执行动画（从上掉落）
        setTimeout(() => {
            markerElement.style.opacity = "1";
            markerElement.style.transform = "translateY(0) scale(1)";
        }, 100);

        return marker;
    } catch (error) {
        console.error('Error adding marker:', error);
        return null;
    }
}

let markersArray = []; // 存储所有 marker 以便清除


// **清除所有 marker**
function clearMarkers() {
    markersArray.forEach(marker => marker.map = null); // 从地图上移除
    markersArray = []; // 清空数组
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
