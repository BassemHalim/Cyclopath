//package com.bassemHalim.cyclopath.User;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
////@DynamoDBTable(tableName = "Cyclopath")
////public class User implements UserDetails {
//public class User {
//
//    private static final String PK_PREFIX = "USER#";
//    private static final String SK_PREFIX = "PROFILE#";
//
//
//    //    @DynamoDBAttribute
////    @Generated
////    private UUID id;
////    @DynamoDBAttribute
//    private String username;
//    //    @DynamoDBAttribute
//    private String email;
//    @NotNull
//    @DynamoDBAttribute
//    private String password;
////    @DynamoDBAttribute
////    private Role role;
//
//    @DynamoDBHashKey(attributeName = "email") //partition key
//    public String getEmail() {
//        return email;
//    }
//
//    //    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        return List.of(new SimpleGrantedAuthority(role.name()));
////    }
//
//    @DynamoDBRangeKey(attributeName = "username") //sort key
////    @Override
//    public String getUsername() {
//        return this.username;
//    }
//
//    //    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    //    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    //    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    //    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
