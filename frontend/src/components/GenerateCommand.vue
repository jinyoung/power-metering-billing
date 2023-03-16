<template>

    <v-card outlined>
        <v-card-title>
            Generate
        </v-card-title>

        <v-card-text>
            <Number label="GeneratedAmount" v-model="value.generatedAmount" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="generate"
            >
                Generate
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
        name: 'GenerateCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.generatedAmount = 0;
        },
        watch: {
        },
        methods: {
            generate() {
                this.$emit('generate', this.value);
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

