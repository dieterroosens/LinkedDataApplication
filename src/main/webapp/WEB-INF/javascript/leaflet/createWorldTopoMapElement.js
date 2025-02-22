var app = (function () {
    return {
        features: [],

        /**
         * Clears the map contents.
         */
        clearMap: function () {
            var i;

            // Reset the remembered last string (so that we can clear the map,
            //  paste the same string, and see it again)
            document.getElementById('wkt').last = '';

            for (i in this.features) {
                if (this.features.hasOwnProperty(i)) {
                    this.map.removeLayer(this.features[i]);
                }
            }
            this.features.length = 0;
        },

        /**
         * Clears the current contents of the textarea.
         */
        clearText: function () {
            document.getElementById('wkt').value = '';
        },

        /**
         * Maps the current contents of the textarea.
         * @param   editable    {Boolean}   Indicates that the feature drawn should be editable
         * @param   focus       {Boolean}   Indicates that the map should pan and/or zoom to new features
         * @return              {Object}    Some sort of geometry object
         */
        mapIt: function (editable, focus) {
            var config, el, obj, wkt;

            // Indicates that the map should pan and/or zoom to new features
            focus = focus || false;

            if (editable === undefined) {
                editable = true;
            }

            el = document.getElementById('wkt');
            //el = element;
            wkt = new Wkt.Wkt();

            if (el.last === el.value) { // Remember the last string
                return; // Do nothing if the WKT string hasn't changed
            } else {
                el.last = el.value;
            }

            try { // Catch any malformed WKT strings
                wkt.read(el.value);
            } catch (e1) {
                try {
                    wkt.read(el.value.replace('\n', '').replace('\r', '').replace('\t', ''));
                } catch (e2) {
                    if (e2.name === 'WKTError') {
                        alert('Wicket could not understand the WKT string you entered. Check that you have parentheses balanced, and try removing tabs and newline characters.');
                        return;
                    }
                }
            }

            config = this.map.defaults;
            config.editable = editable;

            obj = wkt.toObject(this.map.defaults); // Make an object

            // Add listeners for overlay editing events
            if (wkt.type === 'polygon' || wkt.type === 'linestring') {
            }

            if (Wkt.isArray(obj)) { // Distinguish multigeometries (Arrays) from objects
                for (i in obj) {
                    if (obj.hasOwnProperty(i) && !Wkt.isArray(obj[i])) {
                        obj[i].addTo(this.map);
                        this.features.push(obj[i]);
                    }
                }
            } else {
                var popup = document.getElementById('content').value;
                obj.addTo(this.map).bindPopup(popup); // Add it to the map
                this.features.push(obj);
            }
/*
            "    L.marker(["+mapElement.giveLeafletPoint()+"]).addTo(mymap)\n" +
            "        .bindPopup(\""+mapElement.getPopupText()+"\").openPopup();\n" +
            //"        .bindPopup(\"Testing popup\").openPopup();\n" +
            "    var popup = L.popup();\n" +
*/

            // Pan the map to the feature
            if (focus && obj.getBounds !== undefined && typeof obj.getBounds === 'function') {
                // For objects that have defined bounds or a way to get them
                this.map.fitBounds(obj.getBounds());
            } else {
                if (focus && obj.getLatLng !== undefined && typeof obj.getLatLng === 'function') {
                    this.map.panTo(obj.getLatLng());
                }
            }

            return obj;
        },

        /**
         * Updates the textarea based on the first available feature.
         */
        updateText: function () {
            var wkt = new Wkt.Wkt();
            wkt.fromObject(this.features[0]);
            document.getElementById('wkt').value = wkt.write();
        },

        /**
         * Formats the textarea contents for a URL.
         * @param   checked {Boolean}   The check state of the associated checkbox
         */
        urlify: function (checked) {
            var wkt = new Wkt.Wkt();
            wkt.read(document.getElementById('wkt').value);
            wkt.delimiter = (checked) ? '+' : ' ';
            document.getElementById('wkt').value = wkt.write();
            return wkt;
        },

        /**
         * Application entry point.
         * @return  {<L.map>} The Leaflet instance
         */
        init: function () {
            var leaflet, that;

            that = this;

            leaflet = {
                baseLayers: undefined,
                overlays: undefined
            };

            // Stamen //////////////////////////////////////////////////////////////////////

            leaflet.baseLayers = {
                'Stamen Toner Map': L.tileLayer.provider('Stamen.Toner'),
                'Esri': L.tileLayer.provider('Esri.WorldImagery'),
                'WorldTopoMap': L.tileLayer.provider('Esri.WorldTopoMap'),
                'OpenStreetMap': L.tileLayer.provider('OpenStreetMap'),
                'OpenTopoMap': L.tileLayer.provider('OpenTopoMap'),

            };



            // Configure map ///////////////////////////////////////////////////
            leaflet.map = L.map('canvas', {
                zoomControl: true,
                attributionControl: true
                ,
                layers: [
                    //leaflet.baseLayers['Stamen Toner Map']//black & white display
                    //leaflet.baseLayers['OpenStreetMap']//Best map to see standard
                    leaflet.baseLayers['WorldTopoMap']//mix between a good map and hills(no hill-names displayed)
                    //leaflet.baseLayers['OpenTopoMap'] //see hills for example Carrauntoohil 1036m when zoomed in
                ]
            });

            leaflet.map.defaults = {
                icon: new L.Icon({
                    iconUrl: 'https://arthur-e.github.io/Wicket/red_dot.png',
                    iconSize: [16, 16],
                    iconAnchor: [8, 8],
                    shadowUrl: 'https://arthur-e.github.io/Wicket/dot_shadow.png',
                    shadowSize: [16, 16],
                    shadowAnchor: [8, 8]
                }),
                editable: true,
                color: '#AA0000',
                weight: 3,
                opacity: 1.0,
                editable: true,
                fillColor: '#AA0000',
                fillOpacity: 0.2
            };

            // Set event handlers //////////////////////////////////////////////
            leaflet.map.loaded = false;
            //leaflet.map.setView([10, 20], 2);
            leaflet.map.on('load', function () {
                if (!this.loaded) {
                    this.loaded = true;
                    //leaflet.map.setView([10, 20], 2);
                    //First element
                    document.getElementById('wkt').value = '';
                    /*document.getElementById('wkt').value = 'MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)), ((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), (30 20, 20 15, 20 25, 30 20)))';*/
                    document.getElementById('content').value='<iframe src=\"https://extranet.dienstencheques-vlaanderen.be\" height=\"450\"><p>Browser does not support iframes</p></iframe>';
                }
            });

            // There are no 'edit' events, so let's catch editing at its most
            //  fundamental: the mouseup event on the map
            leaflet.map.on('mouseup', function () {
                that.updateText()
                            //.openOn(mymap);
            });

            // Initialize the map //////////////////////////////////////////////
            leaflet.map.setView([10, 20], 2);

            return leaflet.map;
        }

    };

}());