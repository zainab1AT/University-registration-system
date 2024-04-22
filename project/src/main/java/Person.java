        public class Person {
            protected String name;
            private String contactDetails;

            public Person(String name, String contactDetails) {
                this.name = name;
                this.contactDetails = contactDetails;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContactDetails() {
                return contactDetails;
            }

            public void setContactDetails(String contactDetails) {
                this.contactDetails = contactDetails;
            }
        }