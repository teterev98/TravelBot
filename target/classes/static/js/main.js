function isExist(list, name) {
    let isExist = false;
    for (let i = 0; i < list.length; i++) {
        if (list[i].name === name) {
            return true;
        }
    }

    return isExist;
}

function getIndex(list, name) {
    for (let i = 0; i < list.length; i++ ) {
        if (list[i].name === name) {
            return i;
        }
    }

    return -1;
}



let cityApi = Vue.resource('/travel{/name}');

Vue.component('cityForm', {
    props: ['cities', 'cityAttr'],
    data: function () {
        return {
            name: '',
            description: '',
            id: ''
        }
    },
    watch: {
        cityAttr: function (newVal, oldVal) {
            this.description = newVal.description;
            this.name = newVal.name;
            this.id = newVal.id;
        }
    },
    template:
    '<div>' +
    '<input type="text" placeholder="Write something" v-model="name" />' +
    '<input type="text" placeholder="Write something" v-model="description"  size="200" width="0" />' +
    '<input type="button" value="Save" @click="save" />' +
    '</div>',
    methods: {

        save: function () {
            let city =
                {
                    name: this.name,
                    description: this.description,
                    id: this.id
                };

            if (this.name == null || this.name === '') {
                this.name = 'Неверные данные'
            } else if (this.description == null || this.description === '') {
                this.description = 'Неверные данные'

            } else {
                if (isExist(this.cities, this.name)) {
                    cityApi.update({name: this.name}, city).then(result =>
                        result.json().then(data => {
                            let index = getIndex(this.cities, data.name);
                            this.cities.splice(index, 1, data);
                            this.name = '';
                            this.description = ''
                        })
                    )
                } else {
                    cityApi.save({}, city).then(result =>
                        result.json().then(data => {
                            this.cities.push(data);
                            this.description = '';
                            this.name = ''
                        })
                    )
                }
            }
        }
    }

});

Vue.component('city-row', {
    props: ['city', 'editMethod', 'cities'],
    template: '<div>' +
    '<i>({{ city.id }})</i> {{ city.name }}' + ': ' + '{{ city.description }}' +
    '<span style="position: absolute; right: 0">' +
    '<input type="button" value="Edit" @click="edit" />' +
    '<input type="button" value="X" @click="del" />' +
    '</span>' +
    '</div>',
    methods: {
        edit: function () {
            this.editMethod(this.city);
        },
        del: function () {
            cityApi.remove({name: this.city.name}).then(result => {
                if (result.ok) {
                    this.cities.splice(this.cities.indexOf(this.city), 1)
                }
            })
        }
    }
});

Vue.component('cities-list', {
    props: ['cities'],
    data: function () {
        return {
            city: null
        }
    },
    template:
    '<div style="position: relative; width: 500px;">' +
    '<city-form :cities="cities" :cityAttr="city" />' +
    '<city-row v-for="city in cities" :key="city.name" :city="city" ' +
    ':editMethod="editMethod" :cities="cities" />' +
    '</div>',
    created: function () {
        cityApi.get().then(result =>
            result.json().then(data =>
                data.forEach(city => this.cities.push(city))
            )
        )
    },
    methods: {
        editMethod: function (city) {
            this.city = city;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<cities-list :cities="cities" />',
    data: {
        cities: []
    }
});