$(document).ready( function() {
    getAverages();
    getGreenhouseState();
});

// Query APIs every 3 seconds
window.setInterval( function() {

    // Get state values
    getGreenhouseState();

    // Get averages
    getAverages();

}, 3000 );

// Returns average values for sensor zones
function getAverages() {
    $.ajax({
        url: '/api/averages',
        type: 'GET',
        success: function(response) {
            var data = JSON.parse(response);
            $('#greenhouse-temperature').html( parseInt(data.temperature) + "&deg;<span class='fahrenheit'>F</span>");
            $('#greenhouse-humidity').html( parseInt(data.humidity) + "&nbsp;<span class='fahrenheit'>%</span>");
            $('#greenhouse-light').html( parseInt(data.light) + "&nbsp; <span class='fahrenheit'> lux</span>");
        }
    });
}

// Set component states
function getGreenhouseState() {
    $.ajax({
        type: 'GET',
        url: 'api/state',
        success: function(response) {
            var data = JSON.parse(response);

            // Remove active classes
            $('.component-state-active').removeClass('component-state-active');

            if( data.lights ) {
                $('#lights-on').addClass('component-state-active');
                $('#lights-off').addClass('component-state');
            } else {
                $('#lights-on').addClass('component-state');
                $('#lights-off').addClass('component-state-active');
            }

            if( data.fans ) {
                $('#fans-on').addClass('component-state-active');
                $('#fans-off').addClass('component-state');
            } else {
                $('#fans-on').addClass('component-state');
                $('#fans-off').addClass('component-state-active');
            }

            if( data.pump ) {
                $('#water-on').addClass('component-state-active');
                $('#water-off').addClass('component-state');
            } else {
                $('#water-on').addClass('component-state');
                $('#water-off').addClass('component-state-active');
            }

            if( data.heater ) {
                $('#heater-on').addClass('component-state-active');
                $('#heater-off').addClass('component-state');
            } else {
                $('#heater-on').addClass('component-state');
                $('#heater-off').addClass('component-state-active');
            }

            if( data.vents ) {
                $('#vents-open').addClass('component-state-active');
                $('#vents-closed').addClass('component-state');
            } else {
                $('#vents-open').addClass('component-state');
                $('#vents-closed').addClass('component-state-active');
            }

            if( data.shades ) {
                $('#shades-open').addClass('component-state-active');
                $('#shades-closed').addClass('component-state');
            } else {
                $('#shades-open').addClass('component-state');
                $('#shades-closed').addClass('component-state-active');
            }
        }
    });
}



// 3D Model Functions
if (!Detector.webgl) {
    Detector.addGetWebGLMessage();
}

var container;

var camera, controls, scene, renderer;
var lighting, ambient, keyLight, fillLight, backLight;

var windowHalfX = window.innerWidth / 2;
var windowHalfY = window.innerHeight / 2;

init();
animate();

function init() {

    container = document.createElement('div');
    container.style.zIndex= 999;
    container.style.position='absolute';


    document.getElementById('greenhouse-div').appendChild(container);

    /* Camera */

    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 1000);
    camera.position.z = 10;
    camera.position.y = 8;

    /* Scene */

    scene = new THREE.Scene();
    lighting = true;

    ambient = new THREE.AmbientLight(0xffffff, 1.0);
    scene.add(ambient);

    keyLight = new THREE.DirectionalLight(new THREE.Color('hsl(30, 100%, 75%)'), .5);
    //keyLight.position.set(-50, 0, 50);
    keyLight.position.set(0,65,0);

    fillLight = new THREE.DirectionalLight(new THREE.Color('hsl(240, 100%, 75%)'), 0.75);
    fillLight.position.set(50, 0, 50);

    backLight = new THREE.DirectionalLight(0xffffff, 1.0);
    backLight.position.set(50, 0, -50).normalize();

    /* Set lighting properties */
    ambient.intensity = 0.5;
    //scene.add(keyLight);
    scene.add(fillLight);
    scene.add(backLight);

    /* Model */

    var mtlLoader = new THREE.MTLLoader();
    mtlLoader.setBaseUrl('assets/');
    mtlLoader.setPath('assets/');
    mtlLoader.load('greenhouse.mtl', function (materials) {

        materials.preload();

        /*
         materials.materials.default.map.magFilter = THREE.NearestFilter;
         materials.materials.default.map.minFilter = THREE.LinearFilter;*/

        var objLoader = new THREE.OBJLoader();
        objLoader.setMaterials(materials);
        objLoader.setPath('assets/');
        objLoader.load('greenhouse.obj', function (object) {

            var mesh = object;
            mesh.position.y = 0;
            mesh.rotation.y = 100;
            scene.add( mesh );
        });

    });

    //mesh.rotation.x = Math.PI / 2;

    /* Renderer */

    renderer = new THREE.WebGLRenderer( {alpha: true});
    renderer.setClearColor(0x000000, 0);
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize( $('#greenhouse-div').width(), $('#greenhouse-div').height() );
    //renderer.setSize(1000,650);
    //renderer.setClearColor(new THREE.Color("hsl(0, 0%, 10%)"));

    container.appendChild(renderer.domElement);

    /* Controls */

    controls = new THREE.OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.25;
    controls.enableZoom = false;

    /* Events */

    window.addEventListener('resize', onWindowResize, false);
    window.addEventListener('keydown', onKeyboardEvent, false);

}

function onWindowResize() {

    windowHalfX = window.innerWidth / 2;
    windowHalfY = window.innerHeight / 2;

    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

}

function onKeyboardEvent(e) {

    if (e.code === 'KeyL') {

        lighting = !lighting;

        if (lighting) {

            ambient.intensity = 0.75;
            scene.add(keyLight);
            scene.add(fillLight);
            scene.add(backLight);

        } else {

            ambient.intensity = 1.0;
            scene.remove(keyLight);
            scene.remove(fillLight);
            scene.remove(backLight);

        }

    }

}

function animate() {

    requestAnimationFrame(animate);

    controls.update();

    render();

}

function render() {

    renderer.render(scene, camera);

}
