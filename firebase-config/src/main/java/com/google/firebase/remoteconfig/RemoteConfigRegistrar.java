// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
//
// You may obtain a copy of the License at
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.remoteconfig;

import android.content.Context;
import androidx.annotation.Keep;
import com.google.firebase.FirebaseApp;
import com.google.firebase.abt.FirebaseABTesting.OriginService;
import com.google.firebase.abt.component.AbtComponent;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import com.google.firebase.installations.FirebaseInstallationsApi;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Arrays;
import java.util.List;

/**
 * Registrar for setting up Firebase Remote Config's dependency injections in Firebase Android
 * Components.
 *
 * @author Miraziz Yusupov
 * @hide
 */
@Keep
public class RemoteConfigRegistrar implements ComponentRegistrar {
  private static final String LIBRARY_NAME = "fire-rc";

  @Override
  public List<Component<?>> getComponents() {
    return Arrays.asList(
        Component.builder(RemoteConfigComponent.class)
            .name(LIBRARY_NAME)
            .add(Dependency.required(Context.class))
            .add(Dependency.required(FirebaseApp.class))
            .add(Dependency.required(FirebaseInstallationsApi.class))
            .add(Dependency.required(AbtComponent.class))
            .add(Dependency.optionalProvider(AnalyticsConnector.class))
            .factory(
                container ->
                    new RemoteConfigComponent(
                        container.get(Context.class),
                        container.get(FirebaseApp.class),
                        container.get(FirebaseInstallationsApi.class),
                        container.get(AbtComponent.class).get(OriginService.REMOTE_CONFIG),
                        container.getProvider(AnalyticsConnector.class)))
            .eagerInDefaultApp()
            .build(),
        LibraryVersionComponent.create(LIBRARY_NAME, BuildConfig.VERSION_NAME));
  }
}
