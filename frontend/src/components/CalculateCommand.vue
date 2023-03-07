<template>

    <v-card outlined>
        <v-card-title>
            Calculate
        </v-card-title>

        <v-card-text>
            <String label="SubscriberId" v-model="value.subscriberId" :editMode="editMode"/>
            <String label="PlatId" v-model="value.platId" :editMode="editMode"/>
            <String label="GeneratorType" v-model="value.generatorType" :editMode="editMode"/>
            <Number label="GeneratedAmount" v-model="value.generatedAmount" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="calculate"
            >
                Calculate
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>
   
    export default {
        name: 'CalculateCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.subscriberId = '';
            this.value.platId = '';
            this.value.generatorType = '';
            this.value.generatedAmount = 0;
        },
        watch: {
        },
        methods: {
            calculate() {
                this.$emit('calculate', this.value);
            },
            close() {
                this.$emit('closeDialog');
            },
            change() {
                this.$emit('input', this.value);
            },
        }
    }
</script>

